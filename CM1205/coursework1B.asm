.586
.model flat, stdcall
.stack 4096
option casemap :none

GetStdHandle proto	:dword
ReadConsoleA proto	:dword, :dword, :dword, :dword, :dword
ExitProcess proto	:dword
WriteConsoleA proto	:dword, :dword, :dword, :dword, :dword
MessageBoxA proto	:dword, :dword, :dword, :dword

STD_INPUT_HANDLE equ -10
STD_OUTPUT_HANDLE equ -11


.DATA
	bufSize = 80
	bufSize_number = 5
	inputHandle DWORD ?
	buffer db bufSize dup(?)
	buffer_number db bufSize_number dup(?)
	bytes_read DWORD ?
	bytes_written dd ?
	outputHandle DWORD ?
	finalNumber dw 0
	numCovert	db	5 DUP(0)
	msg_mainMenu	db	"Press 1 for celsius to fahrenheit, 2 for fahrenheit to celsius, 3 to exit program",0Dh,0Ah
	msg_enterNumber	db	"Enter a number: "
	msg_endpro	db	"3 or invalid input entered, program end"
	actualNumber	dw	0
	convertedNumber	dw	0
	result_string	db	"The result is",0
	asciiBuf	db	5 dup (" ")
.CODE
	main PROC

		;output menu to console
		invoke	GetStdHandle,	STD_OUTPUT_HANDLE 
		MOV	outputHandle,	EAX
		invoke	WriteConsoleA,	outputHandle,	addr	msg_mainMenu,	SIZEOF msg_mainMenu,	addr bytes_written, 0 ;output msg_mainMenu to console
		
		;get user input
		invoke GetStdHandle, STD_INPUT_HANDLE
		MOV inputHandle, EAX
		invoke ReadConsoleA, inputHandle, ADDR buffer, bufSize, ADDR bytes_read, 0

		;look at user input
		MOV	AL, buffer
		CMP	AL, '1'
		JE	celsius	;jump corrisponding area
		CMP	AL, '2'
		JE	fahrenheit	;jump corrisponding area
		JMP	endPro	;end program

		
		

	;convert user input (fahrenheit) to celsius
	fahrenheit:
		invoke	WriteConsoleA,	outputHandle,	addr	msg_enterNumber,	SIZEOF msg_enterNumber,	addr bytes_written, 0	;output msg_enterNumber to console
		invoke ReadConsoleA, inputHandle, ADDR buffer_number, bufSize_number, ADDR bytes_read, 0	;get number to convert
		CALL	asciiToNum	;convert user input
		MOV	DX, actualNumber
		MOV	convertedNumber, DX
		SUB	convertedNumber, 32
		MOV	AX, 5
		MUL	[convertedNumber]	;multiply actualNumber by 5 (value in AX)
		MOV	convertedNumber, AX
		MOV	DX, 0
		MOV	CX, 9
		DIV	CX	;divide convertedNumber by 9 (value in CX)
		MOV	convertedNumber, AX
		JMP	NumToAscii

	;convert user input (celsius) to fahrenheit
	celsius:
		invoke	WriteConsoleA,	outputHandle,	addr	msg_enterNumber, SIZEOF msg_enterNumber, addr bytes_written, 0	;output msg_enterNumber to console
		invoke ReadConsoleA, inputHandle, ADDR buffer_number, bufSize_number, ADDR bytes_read, 0	;get number to convert
		CALL	asciiToNum	;convert user input
		MOV	DX, actualNumber
		MOV	convertedNumber, DX
		MOV	AX, 9
		MUL	[convertedNumber]	;multiply actualNumber by 9 (value in AX)
		MOV	convertedNumber, AX
		MOV	DX, 0
		MOV	CX, 5
		DIV	CX	;divide convertedNumber by 5 (value in CX)
		MOV	convertedNumber, AX
		ADD	convertedNumber, 32
		JMP	NumToAscii
	
	;converts ascii to number
	asciiToNum:
		SUB	bytes_read, 2	; -2 to remove cr
		MOV	EBX, 0
		MOV	AL, byte ptr [buffer_number + EBX]
		SUB	AL, 30h
		add	[actualNumber], AX

		getNext:			;converts from ascii to number
			INC	BX
			CMP	EBX, bytes_read
			JE	cont
			MOV	AX, 10
			MUL	[actualNumber]
			MOV	actualNumber, AX
			MOV	AL, byte ptr [buffer_number + EBX]
			SUB	AL, 30h
			ADD	actualNumber, AX
			JMP	getNext
		
		;return to celsius or fahrenheit
		cont:
			RET

		;converts number to ascii	
		NumToAscii:
			MOV	EAX, LENGTHOF result_string
			invoke WriteConsoleA, outputHandle, addr result_string, EAX, addr bytes_written, 0
			MOV	AX, [convertedNumber]
			MOV	CL, 10
			MOV	EBX, 3
		
		nextNum:				
			DIV	CL
			ADD	AH, 30h
			MOV	byte ptr [asciiBuf + EBX], AH
			DEC	EBX
			MOV	AH, 0
			CMP	AL, 0
			JA	nextNum
			MOV	EAX, 4
			invoke WriteConsoleA, outputHandle, addr asciiBuf, EAX, addr bytes_written, 0
			MOV	byte ptr [asciiBuf + 4], 0
			invoke MessageBoxA, 0, addr asciiBuf, addr result_string, 0
			MOV	EAX, 0
			MOV	EAX, bytes_written
			PUSH	0
			JMP	endDone

	endPro:
		invoke	WriteConsoleA, outputHandle, addr msg_endpro, SIZEOF msg_endpro, addr bytes_written, 0 ;output msg_endpro to console
	endDone:
	main ENDP
END