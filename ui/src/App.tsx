import * as React from 'react';
import './App.css';
import MultiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import {Col, Grid, Row} from 'react-flexbox-grid';
import {Question} from './components/Question';

const logo = require('./logo.svg');

const styles = {
    wrapper: {
        margin: 10
    }
};

const data = {
    questions: [
        {
            content: 'What is the difference between an ArrayList and a LinkedList?',
            labels: ['NEW_GRAD', 'DATA_STRUCTURES', 'JAVA']
        },
        {
            content: 'What is the worst-case time complexity of an INSERT on a LinkedList?',
            labels: ['NEW_GRAD', 'DATA_STRUCTURES', 'JAVA', 'COMPLEXITY']
        },
        {
            content: 'What is Object-Oriented Programming?',
            labels: ['NEW_GRAD', 'THEORY']
        },
        {
            content: 'Explain Liskov substitution; why is it important for software design?',
            labels: ['NEW_GRAD', 'THEORY', 'DESIGN']
        },
        {
            content: 'What is encapsulation?',
            labels: ['NEW_GRAD', 'THEORY']
        },
        {
            content: 'What is the worst-case time complexity of an INSERT on an ArrayList? What about a DELETE?',
            labels: ['NEW_GRAD', 'THEORY', 'DESIGN']
        },
    ]
};

interface Question {
    content: String,
    labels: String[];
}

class App extends React.Component<{}, {}> {
    render() {
        return (
            <MultiThemeProvider>
                <div className="App">
                    <div className="App-header">
                        <img src={logo} className="App-logo" alt="logo"/>
                        <h2>CX Interview: Question Manager</h2>
                    </div>
                    <div style={styles.wrapper}>
                        <Grid fluid={true}>
                            <Row>
                                {data.questions.map(function (question: Question) {
                                    return (<Col xs={4}>
                                        <Question content={question.content}
                                                  labels={question.labels}/>
                                    </Col>);
                                })}
                            </Row>
                        </Grid>
                    </div>
                </div>
            </MultiThemeProvider>
        );
    }
}

export default App;
