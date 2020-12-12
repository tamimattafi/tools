#include <iostream>

using namespace std;

const char INPUT_SEPARATOR = ' ';
const char MINUS_SIGN = '-';
const int ZERO_ASCII_POSITION = 48;
const int NINE_ASCII_POSITION = ZERO_ASCII_POSITION + 9;

int extractNextInt(string * inputPointer, char delimiter) {
    bool hasMetFirstInteger = false, isNegative = false;
    int value = INT32_MAX;
    int initialValue, actualValue;

    while (!inputPointer->empty()) {
        string inputValue = *inputPointer;
        unsigned char currentChar = inputValue[0];
        inputPointer->erase(0, 1);

        if (currentChar == delimiter && hasMetFirstInteger) break;
        else if (currentChar == MINUS_SIGN) {
            isNegative = true;
            continue;
        } else {
            initialValue = (int) currentChar;
            if (initialValue >= ZERO_ASCII_POSITION && initialValue <= NINE_ASCII_POSITION) {
                actualValue = initialValue - ZERO_ASCII_POSITION;
                if (value == INT32_MAX) {
                    value = actualValue;
                    hasMetFirstInteger = true;
                    continue;
                } else {
                    value = value * 10 + actualValue;
                    continue;
                }
            } else continue;
        }
    }

    if (value == INT32_MAX) return value;
    else return value * (isNegative ? -1 : 1);
}

int main() {
    string input;
    getline(cin, input);

    int inputInteger;
    while ((inputInteger = extractNextInt(&input, INPUT_SEPARATOR)) != INT32_MAX) {
        cout << inputInteger << '\n';
    }
}
