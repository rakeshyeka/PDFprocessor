#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/Zapfino-4.1d6.dfont

import os, shutil, tempfile, fontforge;

results = tempfile.mkdtemp('.tmp','fontforge-test-')

print "This font has odd 'morx' tables, we just don't want to crash"
zapfino = fontforge.open(os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts" "Zapfino-4.1d6.dfont"))
zapfino.generate(os.path.join(results, "Zapfino.ttf"),flags=("apple",));
zapfino.close()

shutil.rmtree(results)
