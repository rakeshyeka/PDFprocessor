#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge

import os, shutil, tempfile, fontforge;

results = tempfile.mkdtemp('.tmp','fontforge-test-')

newfont = fontforge.font()
newfont.save(os.path.join(results, "foo.sfd"));
print "...Saved new font"
newfont.close();

shutil.rmtree(results)
