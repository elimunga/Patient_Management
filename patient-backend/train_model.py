import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler
import joblib

# 1. Load the dataset (Make sure you download the CSV from Kaggle)
df = pd.read_csv("Maternal Health Risk Data Set.csv")

# 2. Separate features (X) and target labels (y)
X = df.drop('RiskLevel', axis=1) 
y = df['RiskLevel'] # Typically: "low risk", "mid risk", "high risk"

# 3. Split the data
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# 4. Scale the features (k-NN is distance-based, so scaling is critical)
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 5. Train the k-NN model
knn = KNeighborsClassifier(n_neighbors=5)
knn.fit(X_train_scaled, y_train)

# Check accuracy
accuracy = knn.score(X_test_scaled, y_test)
print(f"Model Accuracy: {accuracy * 100:.2f}%")

# 6. Export the model and the scaler
joblib.dump(knn, "maternal_knn_model.pkl")
joblib.dump(scaler, "maternal_scaler.pkl")
print("Model and scaler saved successfully!")