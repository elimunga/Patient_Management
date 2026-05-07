# Patient Management System (Full-Stack Assessment)

A complete mobile application and RESTful backend service for registering patients, recording vitals, and conducting dynamic health assessments using both BMI routing and AI-driven predictions.

## 🚀 Tech Stack
* **Frontend:** Android (Java), Material Design UI, Retrofit for API networking.
* **Backend:** Python (FastAPI) for high-performance RESTful endpoints.
* **Machine Learning:** `scikit-learn`, `pandas`, and `joblib` for building and serving k-NN classification models.
* **Database:** PostgreSQL.

## ✨ Key Features
* **AI Maternal Health Prediction:** Integrates a trained k-Nearest Neighbors (k-NN) classification model to dynamically predict maternal health risk levels based on patient vitals.
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
* **Train the AI Model:** Run `python train_model.py` to process the Kaggle dataset and generate the `.pkl` model weights.
* Run the server: `uvicorn main:app --host 0.0.0.0 --port 8000 --reload`

### 2. Frontend Setup (Android)
* Open the `PatientTracker` folder in Android Studio.
* Update the `BASE_URL` in `RetrofitClient.java` to match your server's IP address.
* Run the app on a physical device or emulator.