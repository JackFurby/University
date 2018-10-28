function  [LL, LC, CC] = intersects(lines, circles) 
% --- help for intersects ---
% 
%    Input, matrix containg a set of lines. Each line will have 4 numbers 
%    (x1, y1, x2, y2) corresponding to points on the line. e.g. 
%    [1,2,3,4;3,4,2,1]
%
%    Input, matrix containg a set of circles. Each line will have 3 numbers (xc, yc, r)
%    representing a circle centred at (xc, yc) with radius r. e.g. 
%    [1,2,3;3,4,2]
%
%    Output, matrixes LL, LC and CC containing interception points for
%    lines, lines and circles and circles respectivly. Each row of the
%    matrixes contain the X and Y cordanates of a intersection point.

lineObj = [];

% gets the equation for input lines in parametric format
lineObj = [lines(:, 3)-lines(:, 1), lines(:, 4)-lines(:, 2), lines(:, 1), lines(:, 2)];

%gets all possible pairs of lines
[lineCount,lineParts] = size(lineObj);
lineCount = 1:lineCount;
lineComparisons = nchoosek(lineCount,2);

lips=[]; % line intercept points

% returns matrice of intercept points for lines
for row=1:size(lineComparisons)[1];
    f1 = lineObj(lineComparisons(row, 1), 1);
    g1 = lineObj(lineComparisons(row, 1), 2);
    x1 = lineObj(lineComparisons(row, 1), 3);
    y1 = lineObj(lineComparisons(row, 1), 4);
    f2 = lineObj(lineComparisons(row, 2), 1);
    g2 = lineObj(lineComparisons(row, 2), 2);
    x2 = lineObj(lineComparisons(row, 2), 3);
    y2 = lineObj(lineComparisons(row, 2), 4);
    
    [t1, t2, ip] = lines_par_int_2d (f1, g1, x1, y1, f2, g2, x2, y2);
    if ((0 <= t1 && t1 <= 1) && (0 <= t2 && t2 <= 1))
        lips = [lips; ip];
    end
end

LL = lips; % set intersects to LL, 1 point per row

%gets all possible pairs of lines and circles
[circleCount, circleParts] = size(circles);
[lineCount, lineParts] = size(lines);
LineCircleComparisons = combvec(1:circleCount,1:lineCount);
[pairs, lineCirclePairs] = size(LineCircleComparisons);

clips=[]; % circle line intercept points

% returns matrice of intercept points for lines and circles
for currentCircle=1:lineCirclePairs
    r = circles(LineCircleComparisons(1, currentCircle), 3);
    center = [circles(LineCircleComparisons(1, currentCircle), 1), circles(LineCircleComparisons(1, currentCircle), 2)];
    x0 = lineObj(LineCircleComparisons(2, currentCircle), 3);
    y0 = lineObj(LineCircleComparisons(2, currentCircle), 4);
    f = lineObj(LineCircleComparisons(2, currentCircle), 1);
    g = lineObj(LineCircleComparisons(2, currentCircle), 2);
    
    [num_int, t1, t2, lcip] = circle_imp_line_par_int_2d (r, center, x0, y0, f, g);
    if num_int == 1 && (0 < t1 && t1 < 1)
        clips = [clips lcip(:,1)];
    elseif num_int == 2
        if (0 <= t1 && t1 <= 1)
            clips = [clips lcip(:,1)];
        end
        if (0 <= t2 && t2 <= 1)
            clips = [clips lcip(:,2)];
        end
    end
end

LC = clips'; % set intersects to LC, columns are turned into rows

%gets all possible pairs of circles
[circleCount,circleParts] = size(circles);
circleCount = 1:circleCount;
circleComparisons = nchoosek(circleCount,2);

cips=[]; % circle intercept points

% returns matrice of intersects points for circles
for currentCircle=1:size(circleComparisons)[1];
    r1 = circles(circleComparisons(currentCircle, 1), 3);
    center1 = [circles(circleComparisons(currentCircle, 1), 1), circles(circleComparisons(currentCircle, 1), 2)];
    r2 = circles(circleComparisons(currentCircle, 2), 3);
    center2 = [circles(circleComparisons(currentCircle, 2), 1), circles(circleComparisons(currentCircle, 2), 2)];
    
    [num_int, cip] = circles_imp_int_2d (r1, center1, r2, center2);
    if num_int == 1;
        cips = [cips cip(:,1)];
    elseif num_int == 2;
        cips = [cips cip];
    end
end

CC = cips'; % set intersects to CC, columns are turned into rows

