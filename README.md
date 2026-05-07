# Patient Management System 

A complete mobile application and RESTful backend service for registering patients, recording vitals, and conducting dynamic health assessments based on BMI calculations.

## 🚀 Tech Stack
* **Frontend:** Android (Java), Material Design UI, Retrofit for API networking.
* **Backend:** Python (FastAPI) for high-performance RESTful endpoints.
* **Database:** PostgreSQL.

## ✨ Key Features
* **Dynamic Routing:** Vitals are processed to automatically route the patient to either a General Assessment or an Overweight Assessment based on their auto-calculated BMI.
* **Live Calculation:** Android UI uses `TextWatcher` to calculate BMI in real-time as the user types.
* **Smart UI/UX:** Material Design components with auto-formatting date inputs (YYYY-MM-DD).

## 🛠️ How to Run Locally
Clone this repository.


### 1. Backend Setup (Python/FastAPI)
* Navigate to the `patient-backend` folder.
* Create a virtual environment: `python -m venv venv`
* Activate it: 
    * Windows: `.\venv\Scripts\activate`
    * Mac/Linux: `source venv/bin/activate`
* Install dependencies: `pip install -r requirements.txt`
* **Configuration:** Create a `.env` file in the backend root based on `.env.example` and enter your local PostgreSQL credentials.
* Run the server: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`

### 2. Frontend Setup (Android)
* Open the `PatientTracker` folder in Android Studio.
* Update the `BASE_URL` in `RetrofitClient.java` to match your server's IP address.
* Run the app on a physical device or emulator.