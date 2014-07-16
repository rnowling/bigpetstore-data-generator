file = paper

pdf:
	TEXINPUTS=$$PWD/styles:$$TEXINPUTS pdflatex ${file}
	TEXINPUTS=$$PWD/styles:$$TEXINPUTS bibtex ${file} || true
	TEXINPUTS=$$PWD/styles:$$TEXINPUTS pdflatex ${file}
	TEXINPUTS=$$PWD/styles:$$TEXINPUTS pdflatex ${file}
	rm -f ${file}.log ${file}.aux ${file}.bbl ${file}.blg

clean:
	rm -f ${file}.pdf ${file}.log ${file}.aux ${file}.bbl ${file}.blg
