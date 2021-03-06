import * as React from "react";

export interface Props {
    name: string;
    enthusiasmLevel?: number;
}

/*
 * An example in TSX of a function that returns a React component.
 * Includes an interface and external function support.
 * */
function Hello({name, enthusiasmLevel = 1}: Props) {
    if (enthusiasmLevel <= 0) {
        throw new Error('You could be a little more enthusiastic. :D');
    }

    return (
        <div className="hello">
            <div className="greeting">
                Hello {name + getExclamationMarks(enthusiasmLevel)}
            </div>
        </div>
    );
}

export default Hello;

function getExclamationMarks(numChars: number) {
    return Array(numChars + 1).join('!');
}