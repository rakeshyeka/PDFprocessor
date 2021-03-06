#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/nuvo-medium-woff-demo.woff

import os, shutil, tempfile, fontforge;

results = tempfile.mkdtemp('.tmp','fontforge-test-')

woff=fontforge.open(os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts", "nuvo-medium-woff-demo.woff"));
if ( woff.woffMajor!=7 ) :
  raise ValueError, "Wrong return from woffMajor"
if ( woff.woffMinor!=504 ) :
  raise ValueError, "Wrong return from woffMinor"
if ( len(woff.woffMetadata)!=959 ) :
  raise ValueError, "Wrong return from woffMetadata"

woff.woffMajor = 8;

woff.generate(os.path.join(results, "Foo.woff"));

wofftest=fontforge.open(os.path.join(results, "Foo.woff"));
if ( wofftest.woffMajor!=8 | wofftest.woffMinor!=504 ) :
  raise ValueError, "Wrong return from woffMajor woffMinor after saving"
if ( len(wofftest.woffMetadata)!=959 ) :
  raise ValueError, "Wrong return from woffMetadata after saving"

shutil.rmtree(results)
