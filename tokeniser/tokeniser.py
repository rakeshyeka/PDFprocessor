#!/usr/bin/env python
# -*- coding: utf-8 -*-

# Arun JVS
# utf-8 encoded Hindi text tokeniser

import sys, re, codecs

def fix_inner_punctuation(sentence):
	sentence = sentence.replace(","," , ")
	sentence = sentence.replace("-"," - ")
	sentence = sentence.replace("•","")
	return sentence
	

def main(argc, argv):

	if(argc!=3):
		sys.stderr.write("Usage: " + argv[0] + " input output\n")
		return -1

	line_delimiter = re.compile(ur".*?[?!|•]")
	finp = codecs.open(argv[1], "r", encoding="utf-8")
	content = finp.read()
	content = content.replace("\n", " ")
	content = content.replace(u"।", u"|")
	sentences = line_delimiter.findall(content)
	print len(sentences)
	finp.close()
	
	fout = codecs.open(argv[2], "w", encoding="utf-8")
	for i in range(len(sentences)):
		sentence = sentences[i][:-1]+" "+sentences[i][-1]
		sentence = fix_inner_punctuation(sentence)
		sentence = re.sub(r"( )+", " ", sentence)
		fout.write(sentence + "\n")
	fout.close()
	return 0

if (__name__=="__main__"):
	exit(main(len(sys.argv), sys.argv))

