function [ matrix ] = cellarray2matrix(cellarray,row,col)
%converts a cellarray to m-n matrix
matrix = zeros(row,col);
for i = 1:row-1
    for j = 1:(col)
        
        value = str2double(cellarray{(j+(i-1)*col)});
        if (isempty(value) || isnan(value))
            value = 0;
        end
        matrix(i,j) = value;
        
    end
end

end

