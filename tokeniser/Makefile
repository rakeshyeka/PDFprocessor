all:
	@mkdir -p $(outdir)
	@for file in $$(find $(indir) -type f);                          \
	do                                                               \
		echo "Tokenising $$file to $(outdir)/$$(basename $$file).tok"; \
		./tokeniser.py $$file $(outdir)/$$(basename $$file).tok;       \
	done;                                                            \

help:
	@echo ''
	@echo 'Make scripts usage for tokeniser'
	@echo '	all indir="<indir>" outdir="<outdir>"	-	tokenise all files in <indir> to <outdir>'
	@echo ''

