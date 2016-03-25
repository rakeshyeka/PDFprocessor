#!/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/fontforgeexe/fontforge
#Needs: fonts/DirectionTest.sfd

import os, fontforge;

directiontest = os.path.join("/home/rakesh/pdf2htmlEX/fontforge-pdf2htmlEX/tests", "fonts", "DirectionTest.sfd")

font=fontforge.open(directiontest)
font.selection.all()
font.selection.select(("less",),"K")

dir=True
for g in font.selection.byGlyphs :
  c = g.foreground[0]
  if ( dir != c.isClockwise() ) :
    raise ValueError, "Wrong direction"
  dir = not dir

c = font["K"].foreground[0]
if ( c.isClockwise()!=-1 ) :
  raise ValueError, "Found a direction"

