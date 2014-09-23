\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{idaphdthesis}
              [2006/01/19
               LaTeX idaphdthesis class]

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{idathesis}}

\ProcessOptions

\RequirePackage[latin1]{inputenc}

\LoadClass{idathesis}

\faculty{\newcommand{\issn}{0345--7524}}{\newcommand{\issn}{0282--9800}}
\renewcommand{\idathesistype}{phd}
\renewcommand{\isphd}[1]{#1}

\renewcommand{\maketitle}{
  \renewcommand{\plainheadrulewidth}{0pt}
  \enlargethispage{0mm}
  {
    \vspace*{-15mm}
    \center
    \faculty{%
      {\footnotesize Linköping Studies in Science and Technology}\\[2mm]
      {\footnotesize Dissertations. No. \@thesisnumber}\\[8mm]}
    {%
      {\footnotesize Linköping Studies in Arts and Science}\\[2mm]
      {\footnotesize \@departmentseries}\\[2mm]
      {\footnotesize Dissertations. No. \@thesisnumber}\\[8mm]
    }
    {\LARGE\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}}\\[8mm]
    \swedishenglish{av}{by}
    \\[10mm]
    {\Large\textbf{\@author}}
    \vfill
    %%\\[6mm]{\today}\\[105mm]
    \footnotesize
    \swedishenglish{%
    Institutionen för datavetenskap\\
    Linköpings universitet\\
    581\,83 Linköping%
    }{%
    Department of Computer and Information Science\\
    Linköping University\\
    SE-581\,83 Linköping, Sweden}%
    \\[7mm]
    Linköping \@publicationyear\\
    \vspace*{1mm}
  }
  \newpage
  \faculty{}{%
    Within the Faculty of Arts and Science at Linköping University,
    research and doctorial training is carried out within broad
    problem areas. Research is organized in interdisciplinary research
    environments, doctoral studies mainly in research
    institutes. Together they publish the series Linköping Studies in
    Arts and Science. This thesis comes from \@degreesubject\ at the
    Department of Computer and Information Science.
}
  \mbox{}
  \vfill
  \begin{center}
\footnotesize
Copyright \copyright\ \@publicationyear\ \@publicationmonth
\\[4mm]
ISBN \@isbn\\
ISSN \issn\\
\swedishenglish{Tryckt av LiU Tryck \@publicationyear}{Printed by LiU Tryck \@publicationyear}
\\[4mm]
URL: \@thesisurl
  \end{center}
}



\newcommand{\makeintropages}{
  %% pdfgraphics defines the standard file extension for includegraphics
  %%\pdfgraphics
  \changetext{}{}{}{5mm}{}
  \pagestyle{empty}
  \pagenumbering{roman}
  \maketitle
  \thispagestyle{empty}
  \cleardoublepage
  \changetext{}{}{}{-5mm}{}
%%  \changepage{-40mm}{-14mm}{7mm}{7mm}{0mm}{25mm}{}{}{}
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
}
