import * as React from "react";
import {Component} from "react";

interface Props {
}
interface State {
    value: any;
}

/*
 * An example in TSX of a stateful React component with callbacks.
 */
class Counter extends Component<Props, State> {
    state = {value: 0};

    increment = () => {
        this.setState(prevState => ({
            value: prevState.value + 1
        }));
    };

    decrement = () => {
        this.setState(prevState => ({
            value: prevState.value - 1
        }));
    };

    render() {
        return (
            <div>
                {this.state.value}
                <button onClick={this.increment}>+</button>
                <button onClick={this.decrement}>-</button>
            </div>
        );
    }
}

export default Counter;