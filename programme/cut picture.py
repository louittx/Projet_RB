from flask import Flask, request, send_file
from PIL import Image
import os

app = Flask(__name__)
UPLOAD_FOLDER = "uploads"
OUTPUT_FOLDER = "output"
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(OUTPUT_FOLDER, exist_ok=True)

@app.route('/upload', methods=['POST'])
def upload_image():
    print("Starting upload...")
    
    if 'image' not in request.files:
        print("No image provided")
        return "No image provided", 400
    
    file = request.files['image']
    file_path = os.path.join(UPLOAD_FOLDER, file.filename)
    
    try:
        # Sauvegarde du fichier original
        print(f"Saving image to {file_path}...")
        file.save(file_path)
        
        # Ouvre l'image
        image = Image.open(file_path)
        print(f"Image opened: {image.size}")
        width, height = image.size
        
        # Découper l'image en deux parties
        left_image = image.crop((0, 0, width // 2, height))
        right_image = image.crop((width // 2, 0, width, height))
        
        # Définir les chemins pour enregistrer les images découpées
        left_path = os.path.join(OUTPUT_FOLDER, "left_" + file.filename)
        right_path = os.path.join(OUTPUT_FOLDER, "right_" + file.filename)
        
        # Sauvegarder les images découpées
        print(f"Saving left image to {left_path}...")
        left_image.save(left_path)
        
        print(f"Saving right image to {right_path}...")
        right_image.save(right_path)
        
        # Vérifier si les fichiers ont bien été sauvegardés
        if os.path.exists(left_path) and os.path.exists(right_path):
            print("Images successfully saved.")
            return {"left_image": left_path, "right_image": right_path}
        else:
            print("Failed to save cropped images.")
            return "Failed to save cropped images", 500
    
    except Exception as e:
        print(f"An error occurred: {str(e)}")
        return f"An error occurred: {str(e)}", 500

if __name__ == '__main__':
    app.run(debug=True, threaded=False)