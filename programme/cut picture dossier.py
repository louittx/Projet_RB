from flask import Flask, request, jsonify
from PIL import Image, ImageDraw
import os
import zipfile
# print to cmd
#curl -X POST -F file="@C:\Users\Utilisateur\Desktop\oeil.zip" http://127.0.0.1:5000/upload
app = Flask(__name__)

# Récupérer le chemin de base dans le dossier utilisateur
base_path = os.path.join(os.path.expanduser("~"), "Desktop")  # Bureau de l'utilisateur

# Définition des dossiers
UPLOAD_FOLDER = os.path.join(base_path, "uploads")
LEFT_FOLDER = os.path.join(base_path, "left_images")
RIGHT_FOLDER = os.path.join(base_path, "right_images")

# Création des dossiers s'ils n'existent pas
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
os.makedirs(LEFT_FOLDER, exist_ok=True)
os.makedirs(RIGHT_FOLDER, exist_ok=True)

@app.route('/upload', methods=['POST'])
def upload_images():
    if 'file' not in request.files:
        return "No file provided", 400
    
    file = request.files['file']
    zip_path = os.path.join(UPLOAD_FOLDER, file.filename)
    
    try:
        # Sauvegarde du fichier ZIP
        file.save(zip_path)
        
        # Extraction du ZIP
        extract_folder = os.path.join(UPLOAD_FOLDER, os.path.splitext(file.filename)[0])
        with zipfile.ZipFile(zip_path, 'r') as zip_ref:
            zip_ref.extractall(extract_folder)
        
        processed_files = []

        # Parcourir tous les sous-dossiers et fichiers extraits
        for root, dirs, files in os.walk(extract_folder):
            for filename in files:
                if filename.endswith(('.png', '.jpg', '.jpeg', '.bmp', '.gif')):
                    file_path = os.path.join(root, filename)
                    
                    image = Image.open(file_path)
                    width, height = image.size
                    
                    # DUPLIQUER L'IMAGE ORIGINALE
                    left_image = image.copy()
                    right_image = image.copy()

                    # CRÉER UN DESSINATEUR POUR AJOUTER DU BLANC
                    draw_left = ImageDraw.Draw(left_image)
                    draw_right = ImageDraw.Draw(right_image)

                    # REMPLIR LA MOITIÉ DROITE DE BLANC POUR left_image
                    draw_left.rectangle([width//2, 0, width, height], fill="white")

                    # REMPLIR LA MOITIÉ GAUCHE DE BLANC POUR right_image
                    draw_right.rectangle([0, 0, width//2, height], fill="white")

                    # Recréer la même structure dans LEFT_FOLDER et RIGHT_FOLDER
                    relative_path = os.path.relpath(root, extract_folder)  # Chemin relatif dans le ZIP
                    left_subfolder = os.path.join(LEFT_FOLDER, relative_path)
                    right_subfolder = os.path.join(RIGHT_FOLDER, relative_path)
                    
                    # Création des sous-dossiers si nécessaire
                    os.makedirs(left_subfolder, exist_ok=True)
                    os.makedirs(right_subfolder, exist_ok=True)

                    # Sauvegarde des images transformées
                    left_path = os.path.join(left_subfolder, "left_" + filename)
                    right_path = os.path.join(right_subfolder, "right_" + filename)

                    left_image.save(left_path)
                    right_image.save(right_path)

                    processed_files.append({
                        "original": file_path,
                        "left_image": left_path,
                        "right_image": right_path
                    })

        # Retourner la liste des fichiers traités
        return jsonify(processed_files)
    
    except Exception as e:
        return f"An error occurred: {str(e)}", 500

if __name__ == '__main__':
    app.run(debug=True, threaded=False)
