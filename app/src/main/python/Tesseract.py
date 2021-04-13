# The python logic that performs the actual OCR
#
# Author: Ripley
# Version: 1.0

# adds image processing capabilities
from PIL import Image
# will convert the image to text string
import pytesseract as ocr
import spacy
import re

def Tesseract(imgPath):
    print("hello, the img path is " + imgPath)
    img = Image.open(imgPath)
    print(img)
    # converts the image to result and saves it into result variable
    result = ocr.image_to_string(img, lang='eng', config = "--psm 11")
    result = result.replace("\n\n","\n")

    nlp = spacy.load("en_core_web_lg")
    doc = nlp(result)

    suggestedQuery = ""
    for token in doc:
        if token.pos_ != 'X' and token.is_alpha and token.text != '\n' and len(token.text) > 3:
            if token.pos_ == 'PROPN':
                suggestedQuery += (token.text + " ")
            else:
                suggestedQuery = token.text + " " + suggestedQuery
    return suggestedQuery