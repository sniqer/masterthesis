\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{idalicthesis}
              [2006/01/19
               LaTeX idalicthesis class]


\DeclareOption*{\PassOptionsToClass{\CurrentOption}{idathesis}}

\ProcessOptions

\RequirePackage[latin1]{inputenc}

\LoadClass{idathesis}

\AtEndDocument{\cleardoublepage\librarypage}

\faculty{\newcommand{\issn}{0280--7971}}{\newcommand{\issn}{1401--4637}}

\renewcommand{\idathesistype}{lic}
\renewcommand{\islic}[1]{#1}

\renewcommand{\maketitle}{
  \renewcommand{\plainheadrulewidth}{0pt}
  \enlargethispage{8mm}
  {
    \vspace*{-15mm}
    \center
    \faculty{%
      {\footnotesize Linköping Studies in Science and Technology}\\[2mm]
      {\footnotesize Licentiate Thesis No. \@thesisnumber}\\[8mm]}
    {%
      {\footnotesize Faculty of Arts and Sciences Thesis No.\ \@thesisnumber}\\[8mm]
    }
    {\LARGE\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}}\\[8mm]
    \swedishenglish{av}{by}
    \\[10mm]
    {\Large\textbf{\@author}}
    \vfill
    %%\\[6mm]{\today}\\[105mm]
    \footnotesize
    \swedishenglish{%
    Framlagd vid \faculty{Linköpings tekniska högskola}{filosofiska fakulteten} vid Linköpings universitet\\
    som del av fordringarna för \@degreetype\ licentiatexamen\\[6mm]
    Institutionen för datavetenskap\\
    Linköpings universitet\\
    581\,83 Linköping%
    }{%
%    Submitted to \faculty{Linköping Institute of Technology}{Faculty of Arts and Sciences} at Linköping University in partial\\
%    fulfilment of the requirements for degree of Licentiate of \@degreesubject\\[6mm]
    Department of Computer and Information Science\\
    Linköping University\\
    SE-581\,83 Linköping, Sweden}%
    \\[7mm]
    Linköping \@publicationyear\\
    \vspace*{1mm}
  }
  \newpage
  \mbox{}
  \vfill
  \begin{center}
  \footnotesize
This is a Swedish Licentiate's Thesis
\\[4mm]
Swedish postgraduate education leads to a doctor's degree and/or a licentiate's
degree. A doctor's degree comprises 240 ECTS credits (4 year of full-time studies).\\
A licentiate's degree comprises 120 ECTS credits.
\\[4mm]
Copyright \copyright\ \@publicationyear\ \@publicationmonth
\\[4mm]
ISBN \@isbn\\
ISSN \issn\\
\swedishenglish{Tryckt av LiU Tryck \@publicationyear}{Printed by LiU Tryck \@publicationyear}
\\[4mm]
URL: \@thesisurl
  \end{center}
}


\newcommand{\abstractpage}{%
  \enlargethispage{8mm}
  \vspace*{6mm}
  \begin{center}
    \Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}
  \end{center}
  { \setlength{\parindent}{0mm}
    \footnotesize
    \centerline{\swedishenglish{av}{by}}
    \vspace{3mm}
    \centerline{\@author}
    \vspace{5mm}
    \centerline{\@publicationmonth{} \@publicationyear}
    \centerline{ISBN \@isbn}
    \faculty{%
    \centerline{Linköping Studies in Science and Technology}
    \centerline{Licentiate Thesis No. \@thesisnumber}}{%
    \centerline{Faculty of Arts and Science Thesis No.\ \@thesisnumber}
    }
    \centerline{ISSN \issn}
    \centerline{LiU--Tek--Lic--\@publicationyear:\@thesisyearnumber}
    \vspace{5mm}
    \setlength\parskip{8pt}\centerline{\swedishenglish{SAMMANFATTNING}{ABSTRACT}}
    \vspace{3mm}
    \@abstract
    
    \textit{\swedishenglish{Studien har finansierats av}{This work has been supported by} \@supportedby.}
    \vfill
    \centerline{\swedishenglish{Institutionen för datavetenskap}{Department of Computer and Information Science}}
    \centerline{Linköping University}
    \centerline{\swedishenglish{581\,83 Linköping, Sweden}{SE-581\,83 Linköping, Sweden}}
  }
}

\newcommand{\makeintropages}{
  %% pdfgraphics defines the standard file extension for includegraphics
  %%\pdfgraphics
  \changetext{}{}{}{5mm}{}
%  \changepage{40mm}{14mm}{-7mm}{-7mm}{0mm}{-25mm}{}{}{}
  \pagestyle{empty}
  \pagenumbering{roman}
  \maketitle
  \thispagestyle{empty}
  \cleardoublepage
  \changetext{}{}{}{-5mm}{}
  \thispagestyle{empty}
  \abstractpage
  \cleardoublepage
%  \changepage{-40mm}{-14mm}{7mm}{7mm}{0mm}{25mm}{}{}{}
  \thispagestyle{empty}
  \cleardoublepage
%  \input{acknowledgement.tex}
%  \cleardoublepage

  \pagestyle{fancy}
  \thispagestyle{fancy}
  \tableofcontents
%%  \pagestyle{fancy}
%%  \listoffigures
%%  \pagestyle{fancy}
%%  \listoftables
  \cleardoublepage
  \pagenumbering{arabic}

  %%OLD:
  %%\pagestyle{plain}
  %%\cleardoublepage
  %%\input{acknowledgements.tex}
  %%\cleardoublepage
  %%\pagestyle{fancy}
  %%\tableofcontents
  %%\cleardoublepage
  %%\pagenumbering{arabic}
}



