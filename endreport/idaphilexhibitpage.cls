\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{idaphilexhibitpage}
              [2006/01/19
               LaTeX idaphilexhibitpage class]


\RequirePackage[latin1]{inputenc}

\DeclareOption{lic}{%

\newcommand{\exhibitpagebeforeswedishabstract}{%
%\changetext{}{}{}{5mm}{}
\thispagestyle{empty}
\setlength{\parindent}{0mm}
\faculty{\centerline{\footnotesize Link�ping Studies in Science and Technology}
\vspace{2mm}
\centerline{\footnotesize Licentiate Thesis No.\ \@thesisnumber}}{
\centerline{\footnotesize \swedishenglish{Filosofiska fakulteten FiF-avhandling}{Faculty of Arts and Science FiF-thesis} \@thesisnumber}
}
\vspace{3mm}
{\centering\Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}\\[2mm]}
\centerline{\swedishenglish{av}{by}}
\vspace{2mm}
\centerline{\large\textbf{\@author}}
\vspace{5mm}
\footnotesize
\centerline{\textbf{Licentiatavhandling}}
\vspace{3mm}
\begin{center}%
som f�r avl�ggande av \@degreetype\ licentiatexamen  i \@degreesubject\ vid Link�pings universitet
kommer att offentligt presenteras i \@presentationplace, hus \@presentationhouse, 
Link�pings universitet, \@presentationtime.\end{center}
\setlength\parskip{8pt}%
\begin{center}
\textbf{Fakultetsopponent}\\%
\@facultyexaminername\\
\@facultyexaminertitle\\
\@facultyexamineraddress
\end{center}
\mbox{}\\[3cm]
\begin{center}
%\includegraphics[width=40mm]{LinkUniv_staende_sv}
\includegraphics[width=40mm]{linkuniv_en_vert_blk}
\end{center}
\vfill
\vspace{3mm}
\centerline{\swedishenglish{Institutionen f�r datavetenskap}{Department of Computer and Information Science}}
\centerline{Link�pings universitet}
\centerline{\swedishenglish{581\,83 Link�ping}{SE-581\,83 Link�ping, Sweden}}
\vspace{7mm}
\hspace*{\fill}ISBN \@isbn\hspace*{\fill}\hspace*{\fill}ISSN \issn\hspace*{\fill}
\newpage
\thispagestyle{empty}
%\centerline{\textbf{\swedishenglish{Sammanfattning}{Abstract}}}
\centerline{\textbf{Abstract}}
%\vspace{3mm}\\[-\baselineskip]
}

\newcommand{\exhibitpagebeforeabstract}{%
%\changetext{}{}{}{5mm}{}
\thispagestyle{empty}
\setlength{\parindent}{0mm}
\faculty{\centerline{\footnotesize Link�ping Studies in Science and Technology}
\vspace{2mm}
\centerline{\footnotesize Licentiate Thesis No.\ \@thesisnumber}}{
\centerline{\footnotesize \swedishenglish{Filosofiska fakulteten FiF-avhandling}{Faculty of Arts and Science FiF-thesis} \@thesisnumber}
}
\vspace{3mm}
{\centering\Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}\\[2mm]}
\centerline{\swedishenglish{av}{by}}
\vspace{2mm}
\centerline{\large\textbf{\@author}}
\vspace{5mm}
\footnotesize
\centerline{\textbf{Licentiatavhandling}}
\vspace{3mm}
\begin{center}%
som f�r avl�ggande av filosofie licentiatexamen i \@degreesubject\ vid Link�pings universitet
kommer att offentligt presenteras i \@presentationplace, hus \@presentationhouse, 
Link�pings universitet, \@presentationtime.\end{center}
\setlength\parskip{8pt}%
\begin{center}
\textbf{Fakultetsopponent}\\%
\@facultyexaminername\\
\@facultyexaminertitle\\
\@facultyexamineraddress
\end{center}
\mbox{}\\[3cm]
\begin{center}
%\includegraphics[width=40mm]{LinkUniv_staende_sv}
\includegraphics[width=40mm]{linkuniv_en_vert_blk}
\end{center}
\vfill
\vspace{3mm}
\centerline{\swedishenglish{Institutionen f�r datavetenskap}{Department of Computer and Information Science}}
\centerline{Link�ping University}
\centerline{\swedishenglish{581\,83 Link�ping}{SE-581\,83 Link�ping, Sweden}}
\vspace{7mm}
\hspace*{\fill}ISBN \@isbn\hspace*{\fill}\hspace*{\fill}ISSN \issn\hspace*{\fill}
\newpage
\thispagestyle{empty}
%\centerline{\textbf{\swedishenglish{Sammanfattning}{Abstract}}}
\centerline{\textbf{Abstract}}
%\vspace{3mm}\\[-\baselineskip]
}

\newcommand{\exhibitpageafterswedishabstract}{%

\textit{\swedishenglish{Studien har finansierats av}{This work has been supported} \@supportedby.}\\
URL: \@thesisurl
}

\newcommand{\exhibitpageafterabstract}{%

\textit{\swedishenglish{Studien har finansierats av}{This work has been supported} \@supportedby.}\\
URL: \@thesisurl
}

}

\DeclareOption{master}{% !!!!NOTUSED but needed for other reasons (make)
\newcommand{\exhibitpagebeforeabstract}{%
%\changetext{}{}{}{5mm}{}
\thispagestyle{empty}
\setlength{\parindent}{0mm}
\faculty{\centerline{\footnotesize Link�ping Studies in Science and Technology}
\vspace{2mm}
\centerline{\footnotesize Thesis No. \@thesisnumber}}{
\centerline{\footnotesize \swedishenglish{Filosofiska fakulteten FiF-avhandling}{Faculty of Arts and Science FiF-thesis} \@thesisnumber}
}
\vspace{3mm}
{\centering\Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}\\[2mm]}
\centerline{\swedishenglish{av}{by}}
\vspace{2mm}
\centerline{\large\textbf{\@author}}
\vspace{5mm}
\footnotesize
\centerline{\textbf{Licentiatavhandling}}
\vspace{3mm}
som f�r avl�ggande av filosofie licentiatexamen i vid Link�pings universitet
kommer att offentligt presenteras i \@presentationplace, hus \@presentationhouse, 
Link�pings universitet, \@presentationtime.\\[7mm]
\setlength\parskip{8pt}\centerline{\textbf{\swedishenglish{Sammanfattning}{Abstract}}}
\vspace{3mm}\\[-\baselineskip]
}

\newcommand{\exhibitpageafterabstract}{% !!!!NOTUSED but needed for other reasons (make)

\textit{\swedishenglish{Studien har finansierats av}{This work has been supported} \@supportedby.}
\vfill
\centerline{URL: \@thesisurl}
\vspace{3mm}
\centerline{\swedishenglish{Institutionen f�r datavetenskap}{Department of Computer and Information Science}}
\centerline{\swedishenglish{Link�pings universitet}{Link�ping University}}
\centerline{\swedishenglish{581\,83 Link�ping}{SE-581\,83 Link�ping, Sweden}}
\vspace{7mm}
\hspace*{\fill}ISBN \@isbn\hspace*{\fill}\hspace*{\fill}ISSN \issn\hspace*{\fill}
}
}

\DeclareOption{phd}{%
\newcommand{\exhibitpagebeforeswedishabstract}{%
\thispagestyle{empty}
\setlength{\parindent}{0mm}
\centerline{\footnotesize Link�ping Studies in Arts and Science No. \@thesisnumber}
\vspace{2mm}
\centerline{\footnotesize\@departmentseries}
\vspace{8mm}
%{\centering\Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}\\[2mm]}
{\centering\Large\textbf{\@titleswedish}\\[2mm]}
%\centerline{\swedishenglish{av}{by}}
\vspace{2mm}
\centerline{\footnotesize\textbf{\@author}}
\vspace{5mm}
\footnotesize
\centerline{\textbf{Akademisk avhandling}}
\vspace{3mm}
\begin{center}%
som f�r avl�ggande av \@degreetype\ doktorsexamen i \@degreesubjectswedish\ vid Link�pings 
universitet
kommer att offentligt f�rsvaras i \@presentationplace, \@presentationhus, 
\@presentationdatum, \@presentationtime.\end{center}
\vspace{7mm}
\setlength\parskip{8pt}\centerline{\textbf{Sammanfattning}}
\vspace{3mm}
}


\newcommand{\exhibitpagebeforeabstract}{%
\thispagestyle{empty}
\setlength{\parindent}{0mm}
\centerline{\footnotesize Link�ping Studies in Arts and Science No. \@thesisnumber}
\vspace{2mm}
\centerline{\footnotesize\@departmentseries}
\vspace{3mm}
%{\centering\Large\textbf{\swedishenglish{\@titleswedish}{\@titleenglish}}\\[2mm]}
{\centering\Large\textbf{\@titleenglish}\\[2mm]}
%\centerline{\swedishenglish{av}{by}}
\vspace{2mm}
\centerline{\footnotesize\textbf{\@author}}
\vspace{5mm}
\footnotesize
\centerline{\textbf{Academic dissertation}}
\vspace{3mm}
\begin{center}%
Academic dissertation for the Degree of Doctor of Philosophy in \@degreesubject\ at
Link�ping University to be publicly defended on \@presentationdate\ at \@presentationtime\
in \@presentationplace, \@presentationhouse.\end{center}
\vspace{7mm}
\setlength\parskip{8pt}\centerline{\textbf{Abstract}}
\vspace{3mm}
}

\newcommand{\exhibitpageafterswedishabstract}{%
\vfill
Nyckelord: \@nyckelord\\
\vspace{3mm}\\
\centerline{\swedishenglish{Institutionen f�r datavetenskap}{Department of Computer and Information Science}}
\centerline{Link�pings universitet}
\centerline{\swedishenglish{581\,83 Link�ping}{SE-581\,83 Link�ping, Sweden}}
\vspace{7mm}
\hspace*{\fill}ISBN \@isbn\hspace*{\fill}\hspace*{\fill}ISSN \issn\hspace*{\fill}
}

\newcommand{\exhibitpageafterabstract}{%
\vfill
Keywords: \@keywords\\
\vspace{3mm}\\
\centerline{\swedishenglish{Institutionen f�r datavetenskap}{Department of Computer and Information Science}}
\centerline{Link�ping University}
\centerline{\swedishenglish{581\,83 Link�ping}{SE-581\,83 Link�ping, Sweden}}
\vspace{7mm}
\hspace*{\fill}ISBN \@isbn\hspace*{\fill}\hspace*{\fill}ISSN \issn\hspace*{\fill}
}
}

\DeclareOption*{\PassOptionsToClass{\CurrentOption}{idathesis}}

\ProcessOptions

\LoadClass[exhibitpage]{idathesis}
%\LoadClass{idathesis}

\AtEndDocument{}
