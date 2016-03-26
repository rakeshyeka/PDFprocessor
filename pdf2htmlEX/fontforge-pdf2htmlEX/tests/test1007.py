#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/ayn+meem.init.svg

# At one point splinestroke failed if the first spline on a contour had a length of 0

import os, fontforge;

font=fontforge.font();
a = font.createChar(65);
a.importOutlines(os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts", "ayn+meem.init.svg"));
