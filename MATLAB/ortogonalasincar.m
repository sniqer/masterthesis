tic
f1=1000;
f2=1200;


t1=[0:1/(1000*f1):1];
t2=[0:1/(1000*f1):1];

length(t1)
length(t2)
y1=sin(2*pi*f1*t1);
y2=sin(2*pi*f2*t2);

ffty1 = abs(fft(y1,2^17));
ffty2 = abs(fft(y2,2^18));

plot(y1);
%plot(t1,y1,t2,y2)
toc