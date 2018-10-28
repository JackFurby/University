function varargout = shape_gui(varargin)
% SHAPE_GUI MATLAB code for shape_gui.fig
%      SHAPE_GUI, by itself, creates a new SHAPE_GUI or raises the existing
%      singleton*.
%
%      H = SHAPE_GUI returns the handle to a new SHAPE_GUI or the handle to
%      the existing singleton*.
%
%      SHAPE_GUI('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in SHAPE_GUI.M with the given input arguments.
%
%      SHAPE_GUI('Property','Value',...) creates a new SHAPE_GUI or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before shape_gui_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to shape_gui_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help shape_gui

% Last Modified by GUIDE v2.5 10-Dec-2017 21:09:00

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @shape_gui_OpeningFcn, ...
                   'gui_OutputFcn',  @shape_gui_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before shape_gui is made visible.
function shape_gui_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to shape_gui (see VARARGIN)

% Choose default command line output for shape_gui
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% default axis values
handles.axisX = [-15 15];
handles.axisY = [-15 15];
xlim([-15 15]);
ylim([-15 15]);
axis square

%empty matrices for lines and circles
handles.currentLines = [];
handles.currentCircles = [];
guidata(hObject,handles);
% UIWAIT makes shape_gui wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = shape_gui_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;

% --- draws all lines and circles on the axis.
function drawAxis(lines, circles)

cla
hold on;
for row=1:size(lines)[1];
    line = lines(row,:);
    lineW = [(line(3)-line(1)), (line(4)-line(2))];
        
    t = [0:1];
    x = line(1) + t * lineW(1);
    y = line(2) + t * lineW(2);
    plot(x,y, 'r')
end
 
for row=1:size(circles)[1];
    circle = circles(row,:);
    
    theta = 0:pi/50:2*pi;
    x = circle(3) * cos(theta) + circle(1);
    y = circle(3) * sin(theta) + circle(2);
    plot(x, y, 'b')
end
makeLegend 
hold off

% adds a legend to the axis (the legend does not change)
function makeLegend
legendData = zeros(5, 1);
legendData(1) = plot(NaN,NaN,'-r');
legendData(2) = plot(NaN,NaN,'ob');
legendData(3) = plot(NaN,NaN,'ok');
legendData(4) = plot(NaN,NaN,'om');
legendData(5) = plot(NaN,NaN,'og');
legend(legendData, 'Lines','Circles','Line intersects', 'Circle intersects', 'Line circle intersects');

% --- Executes on button press in linePushbutton.
function linePushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to linePushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% adds one line to currentLines
x2EditText = get(handles.x2EditText, 'String');
x1EditText = get(handles.x1EditText, 'String');
y2EditText = get(handles.y2EditText, 'String');
y1EditText = get(handles.y1EditText, 'String');
if isempty(x2EditText) && isempty(x1EditText) && isempty(y2EditText) && isempty(y1EditText)
    x1 = rand*randi([-10, 10]);
    x2 = rand*randi([-10, 10]);
    y1 = rand*randi([-10, 10]);
    y2 = rand*randi([-10, 10]);
    currentLines = handles.currentLines;
    currentLines = [currentLines; [x1, y1, x2, y2]];
    handles.currentLines = currentLines;
    guidata(hObject,handles);
elseif isempty(x2EditText) || isempty(x1EditText) || isempty(y2EditText) || isempty(y1EditText)
    errordlg('Missing one or more points')
else
    if regexp(x2EditText,'^[-]?\d+$') & regexp(x1EditText,'^[-]?\d+$') & regexp(y2EditText,'^[-]?\d+$') & regexp(y1EditText,'^[-]?\d+$')
        x1 = str2num(x1EditText);
        x2 = str2num(x2EditText);
        y1 = str2num(y1EditText);
        y2 = str2num(y2EditText);
        currentLines = handles.currentLines;
        currentLines = [currentLines; [x1, y1, x2, y2]];
        handles.currentLines = currentLines;
        guidata(hObject,handles);
       
    else
        errordlg('You can only enter numbers')
    end
end

currentCircles = handles.currentCircles;
drawAxis(currentLines, currentCircles)

% --- Executes on button press in circlePushbutton.
function circlePushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to circlePushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% adds one circle to currentCircles
xEdit = get(handles.circleXEdit, 'String');
yEdit = get(handles.circleYEdit, 'String');
rEdit = get(handles.circleREdit, 'String');
if isempty(xEdit) && isempty(yEdit) && isempty(rEdit)
    x = rand*randi([-10, 10]);
    y = rand*randi([-10, 10]);
    r = rand*randi([-5, 5]);
    currentCircles = handles.currentCircles;
    currentCircles = [currentCircles; [x, y, r]];
    handles.currentCircles = currentCircles;
    guidata(hObject,handles);
elseif isempty(xEdit) || isempty(yEdit) || isempty(rEdit)
    errordlg('Missing one or more points')
else
    if regexp(xEdit,'^[-]?\d+$') & regexp(yEdit,'^[-]?\d+$') & regexp(rEdit,'^\d+$')
        x = str2num(xEdit);
        y = str2num(yEdit);
        r = str2num(rEdit);
        currentCircles = handles.currentCircles;
        currentCircles = [currentCircles; [x, y, r]];
        handles.currentCircles = currentCircles;
        guidata(hObject,handles);
    else
        errordlg('You can only enter numbers or r is negative')
    end
end

currentLines = handles.currentLines;
drawAxis(currentLines, currentCircles)



function circleEditText_Callback(hObject, eventdata, handles)
% hObject    handle to circleEditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of circleEditText as text
%        str2double(get(hObject,'String')) returns contents of circleEditText as a double


% --- Executes during object creation, after setting all properties.
function circleEditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to circleEditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function line2EditText_Callback(hObject, eventdata, handles)
% hObject    handle to line2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of line2EditText as text
%        str2double(get(hObject,'String')) returns contents of line2EditText as a double


% --- Executes during object creation, after setting all properties.
function line2EditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to line2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function x1EditText_Callback(hObject, eventdata, handles)
% hObject    handle to x1EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of x1EditText as text
%        str2double(get(hObject,'String')) returns contents of x1EditText as a double


% --- Executes during object creation, after setting all properties.
function x1EditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to x1EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function y1EditText_Callback(hObject, eventdata, handles)
% hObject    handle to y1EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of y1EditText as text
%        str2double(get(hObject,'String')) returns contents of y1EditText as a double


% --- Executes during object creation, after setting all properties.
function y1EditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to y1EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function y2EditText_Callback(hObject, eventdata, handles)
% hObject    handle to y2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of y2EditText as text
%        str2double(get(hObject,'String')) returns contents of y2EditText as a double


% --- Executes during object creation, after setting all properties.
function y2EditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to y2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function x2EditText_Callback(hObject, eventdata, handles)
% hObject    handle to x2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of x2EditText as text
%        str2double(get(hObject,'String')) returns contents of x2EditText as a double


% --- Executes during object creation, after setting all properties.
function x2EditText_CreateFcn(hObject, eventdata, handles)
% hObject    handle to x2EditText (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in findInterceptspushbutton.
function findInterceptspushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to findInterceptspushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% plots markers for intersections

currentCircles = handles.currentCircles;
currentLines = handles.currentLines;

[LL, LC, CC] = intersects(currentLines, currentCircles);
hold on;

for row=1:size(LL)[2];
    point = LL(row,:);
    
    theta = 0:pi/50:2*pi;
    x = 0.3 * cos(theta) + point(1);
    y = 0.3 * sin(theta) + point(2);
    plot(x, y, 'k', 'LineWidth', 3)
end

for row=1:size(LC)[2];
    point = LC(row,:);
    
    theta = 0:pi/50:2*pi;
    x = 0.3 * cos(theta) + point(1);
    y = 0.3 * sin(theta) + point(2);
    plot(x, y, 'g', 'LineWidth', 3)
end

for row=1:size(CC)[2];
    point = CC(row,:);
    
    theta = 0:pi/50:2*pi;
    x = 0.3 * cos(theta) + point(1);
    y = 0.3 * sin(theta) + point(2);
    plot(x, y, 'm', 'LineWidth', 3)
end

makeLegend


function circleXEdit_Callback(hObject, eventdata, handles)
% hObject    handle to circleXEdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of circleXEdit as text
%        str2double(get(hObject,'String')) returns contents of circleXEdit as a double


% --- Executes during object creation, after setting all properties.
function circleXEdit_CreateFcn(hObject, eventdata, handles)
% hObject    handle to circleXEdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function circleYEdit_Callback(hObject, eventdata, handles)
% hObject    handle to circleYEdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of circleYEdit as text
%        str2double(get(hObject,'String')) returns contents of circleYEdit as a double


% --- Executes during object creation, after setting all properties.
function circleYEdit_CreateFcn(hObject, eventdata, handles)
% hObject    handle to circleYEdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function circleREdit_Callback(hObject, eventdata, handles)
% hObject    handle to circleREdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of circleREdit as text
%        str2double(get(hObject,'String')) returns contents of circleREdit as a double


% --- Executes during object creation, after setting all properties.
function circleREdit_CreateFcn(hObject, eventdata, handles)
% hObject    handle to circleREdit (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in clearPushbutton.
function clearPushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to clearPushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% clears axis, currentLines and currentCircles

handles.currentLines = [];
handles.currentCircles = [];
guidata(hObject,handles);
drawAxis(handles.currentLines, handles.currentCircles)


% --- Executes on button press in savePushbutton.
function savePushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to savePushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
[FileName,PathName] = uiputfile('*.mat','Save Workspace As');
if FileName ~= 0
    lines = handles.currentLines;
    circles = handles.currentCircles;
    saveName = fullfile(PathName, FileName);
    save(saveName,'lines', 'circles');
end

% --- Executes on button press in loadPushbutton.
function loadPushbutton_Callback(hObject, eventdata, handles)
% hObject    handle to loadPushbutton (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

[FileName,PathName,FilterIndex] = uigetfile({'*.mat'}, 'File selector');
if FileName ~= 0
    loadName = fullfile(PathName, FileName);
    load(loadName, 'lines', 'circles');
    handles.currentLines = lines;
    handles.currentCircles = circles;
    guidata(hObject,handles);
    drawAxis(lines, circles)
end


% will zoom out on the axis
function uitoggletool4_OnCallback(hObject, eventdata, handles)
% hObject    handle to uitoggletool4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

axisX = handles.axisX;
axisY = handles.axisY;
axisX = axisX.*1.2;
axisY = axisY.*1.2;
xlim(axisX);
ylim(axisY);
axis square
handles.axisX = axisX;
handles.axisY = axisY;
guidata(hObject,handles);


% will zoom in on the axis
function uitoggletool3_OnCallback(hObject, eventdata, handles)
% hObject    handle to uitoggletool3 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

axisX = handles.axisX;
axisY = handles.axisY;
axisX = axisX./1.2;
axisY = axisY./1.2;
xlim(axisX);
ylim(axisY);
axis square
handles.axisX = axisX;
handles.axisY = axisY;
guidata(hObject,handles);
