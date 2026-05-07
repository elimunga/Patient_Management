import os
import psycopg2
from psycopg2.extras import RealDictCursor
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from datetime import date
from typing import List, Optional

# --- NEW DOTENV IMPORTS ---
from dotenv import load_dotenv

# 1. Load the secrets from your .env file into the application
load_dotenv()

# 2. Securely fetch the credentials from the environment
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_HOST = "localhost" 
DB_NAME = "patient_db" # Make sure this matches your actual PostgreSQL database name!

# 3. Initialize FastAPI
app = FastAPI()

# 4. Update your database connection function to use the secure variables
def get_db_connection():
    try:
        conn = psycopg2.connect(
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD,
            host=DB_HOST
        )
        return conn
    except Exception as e:
        print("Secure Database connection failed:", e)
        raise HTTPException(status_code=500, detail="Database connection error")

def get_db_connection():
    return psycopg2.connect(**DB_CONFIG)

# --- Pydantic Models ---
class PatientCreate(BaseModel):
    patient_id: str
    first_name: str
    last_name: str
    dob: date
    gender: str
    registration_date: date

class VitalCreate(BaseModel):
    patient_id: str
    visit_date: date
    height_cm: float
    weight_kg: float
    # BMI is auto-calculated by the backend

class OverweightAssessmentCreate(BaseModel):
    patient_id: str
    visit_date: date
    general_health: str
    diet_history: bool
    comments: Optional[str] = ""

class GeneralAssessmentCreate(BaseModel):
    patient_id: str
    visit_date: date
    general_health: str
    using_drugs: bool
    comments: Optional[str] = ""

# --- Endpoints ---

@app.post("/api/patients/register", status_code=201)
def register_patient(patient: PatientCreate):
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        cursor.execute("SELECT patient_id FROM patients WHERE patient_id = %s", (patient.patient_id,))
        if cursor.fetchone():
            raise HTTPException(status_code=400, detail="Patient ID already registered")

        cursor.execute(
            """INSERT INTO patients (patient_id, first_name, last_name, dob, gender, registration_date)
               VALUES (%s, %s, %s, %s, %s, %s)""",
            (patient.patient_id, patient.first_name, patient.last_name, patient.dob, patient.gender, patient.registration_date)
        )
        conn.commit()
        return {"message": "Patient registered successfully"}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()

@app.post("/api/vitals", status_code=201)
def add_vitals(vital: VitalCreate):
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        # Auto-calculate BMI (Weight in KG / Height in M squared)
        height_m = vital.height_cm / 100.0
        bmi = round(vital.weight_kg / (height_m ** 2), 2)

        cursor.execute(
            """INSERT INTO vitals (patient_id, visit_date, height_cm, weight_kg, bmi)
               VALUES (%s, %s, %s, %s, %s) RETURNING bmi""",
            (vital.patient_id, vital.visit_date, vital.height_cm, vital.weight_kg, bmi)
        )
        conn.commit()
        return {"message": "Vitals saved successfully", "calculated_bmi": bmi}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()

@app.post("/api/assessments/general", status_code=201)
def add_general_assessment(assessment: GeneralAssessmentCreate):
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        cursor.execute(
            """INSERT INTO general_assessments (patient_id, visit_date, general_health, using_drugs, comments)
               VALUES (%s, %s, %s, %s, %s)""",
            (assessment.patient_id, assessment.visit_date, assessment.general_health, assessment.using_drugs, assessment.comments)
        )
        conn.commit()
        return {"message": "General assessment saved"}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()

@app.post("/api/assessments/overweight", status_code=201)
def add_overweight_assessment(assessment: OverweightAssessmentCreate):
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        cursor.execute(
            """INSERT INTO overweight_assessments (patient_id, visit_date, general_health, diet_history, comments)
               VALUES (%s, %s, %s, %s, %s)""",
            (assessment.patient_id, assessment.visit_date, assessment.general_health, assessment.diet_history, assessment.comments)
        )
        conn.commit()
        return {"message": "Overweight assessment saved"}
    except Exception as e:
        conn.rollback()
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()

@app.get("/api/patients", status_code=200)
def get_patient_listing():
    conn = get_db_connection()
    # RealDictCursor returns data as dictionaries, making it easy to convert to JSON
    cursor = conn.cursor(cursor_factory=RealDictCursor) 
    try:
        # A simple query to get patients and their latest BMI
        cursor.execute("""
            SELECT 
                p.first_name || ' ' || p.last_name AS patient_name,
                EXTRACT(YEAR FROM age(p.dob)) AS age,
                v.bmi AS last_bmi,
                v.visit_date AS last_assessment_date
            FROM patients p
            LEFT JOIN LATERAL (
                SELECT bmi, visit_date 
                FROM vitals 
                WHERE patient_id = p.patient_id 
                ORDER BY visit_date DESC LIMIT 1
            ) v ON true
        """)
        patients = cursor.fetchall()
        
        # Determine BMI Status in Python before sending to the app
        for p in patients:
            if p['last_bmi'] is None:
                p['bmi_status'] = "No Vitals Yet"
            elif p['last_bmi'] < 18.5:
                p['bmi_status'] = "Underweight"
            elif p['last_bmi'] < 25:
                p['bmi_status'] = "Normal"
            else:
                p['bmi_status'] = "Overweight"

        return patients
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
    finally:
        cursor.close()
        conn.close()