#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/LucidaGrande.ttc

#Test the fontforge module (but not its types)
import os, fontforge;
font = fontforge.open(os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts", "LucidaGrande.ttc"))
