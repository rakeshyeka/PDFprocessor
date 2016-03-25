#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/Caliban.sfd

import os, shutil, tempfile, fontforge;

results = tempfile.mkdtemp('.tmp','fontforge-test-')

caliban=fontforge.open(os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts", "Caliban.sfd"));
print "...Opened Caliban"
caliban.selection.all()
caliban.autoHint();
print "...AutoHinted";
caliban.save(os.path.join(results, "Caliban.sfd"));
print "...Saved As";
caliban.save(os.path.join(results, "Caliban.sfdir"));
print "...Saved As dir";
caliban.generate(os.path.join(results, "Caliban.ps"),bitmap_type="bdf");
print "...Generated type0"
caliban.generate(os.path.join(results, "Caliban.ttf"),bitmap_type="ms",flags=("pfm",));
print "...Generated ttf w/ ms bitmaps"
caliban.generate(os.path.join(results, "Caliban.ttf"),bitmap_type="apple");
print "...Generated ttf w/ apple bitmaps"
caliban.generate(os.path.join(results, "Caliban.otf"),bitmap_type="ms");
print "...Generated otf w/ ms bitmaps"
caliban.generate(os.path.join(results, "Caliban.otf"),bitmap_type="ttf",flags=("apple","opentype"));
print "...Generated otf w/ both apple and ot modes set (& bitmaps)"
caliban.generate(os.path.join(results, "Caliban.dfont"),bitmap_type="sbit");
print "...Generated sbit"
caliban.generate(os.path.join(results, "Caliban."),bitmap_type="otb");
print "...Generated X11 opentype bitmap"
caliban.generate(os.path.join(results, "Caliban.dfont"),bitmap_type="dfont");
print "...Generated dfont w/ apple bitmaps"
caliban.layers["Fore"].is_quadratic = 1
caliban.setTableData("cvt ","");
caliban.selection.all()
fontforge.setPrefs("DetectDiagonalStems",1);
caliban.autoHint();
caliban.autoInstr();
print"...AutoInstructed";
caliban.generate(os.path.join(results, "Caliban.ttf"),bitmap_type="apple");
print "...Generated ttf w/ apple bitmaps (again) and instructions"
caliban.close();

caliban = fontforge.open(os.path.join(results, "Caliban.sfd"));
print "...Read sfd"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.sfdir"));
print "...Read sfdir"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.ps"));
print "...Read type0 (if PfaEdit didn't understand /CalibanBase that's ok)"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.ttf"));
print "...Read ttf"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.otf"));
print "...Read otf"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.dfont"));
print "...Read dfont"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban.otb"));
print "...Read otb"
caliban.close();
caliban = fontforge.open(os.path.join(results, "Caliban-10.bdf"));
caliban.generate(os.path.join(results, "Caliban.dfont"),bitmap_type="sbit");
print "...Read bdf & Generated sbit"
caliban.close();

shutil.rmtree(results)
