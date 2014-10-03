function [ y ] = mapthroughput2sinr( throughput_vector,sinr_vector )
y = zeros(length(find(sinr_vector)),2);

for i = 1:length(find(sinr_vector))-25
    
    throughput_ind = find(throughput_vector,1,'first');
    sinr_ind = find(sinr_vector(throughput_ind:end),1,'first');
    sinr_ind = sinr_ind + throughput_ind - 1;
    
    y(i,1) = throughput_vector(throughput_ind);
    y(i,2) = sinr_vector(sinr_ind);
    throughput_vector= throughput_vector(throughput_ind+1:end);
    sinr_vector = sinr_vector(throughput_ind+1:end);
end

