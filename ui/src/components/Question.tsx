import * as React from "react";
import Paper from "material-ui/Paper";
import Divider from "material-ui/Divider";
import Chip from "material-ui/Chip";

const styles = {
    wrapper: {
        margin: 4
    },
    subheader: {
        padding: 8,
        textAlign: 'left'
    },
    chips: {
        margin: 8,
        textAlign: 'left',
        display: 'flex',
        flexWrap: 'wrap'
    },
    chip: {
        margin: 4
        // display: "inline-flex"
    }
};

/*
 * Initial pass at the Question component
 * */
export const Question = (props: any) => (
    <div style={styles.wrapper}>
        <Paper zDepth={1}>
            <p style={styles.subheader}>{props.content}</p>
            <div style={styles.chips as any}>
                {props.labels.map(function (label: String) {
                    return <Chip style={styles.chip}>{label}</Chip>
                })}
            </div>
            <Divider/>
            <div style={{margin: 10}}>
                {/*Will likely move this to a context menu to prevent repetition in the cards aesthetic view*/}
                <a href="#">See acceptable answers</a>
            </div>
        </Paper>
    </div>
);