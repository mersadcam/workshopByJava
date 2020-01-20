import React from 'react';
import {Button, Card, Grid, StampCard, StatsCard} from 'tabler-react';

class Summary extends React.Component {
    render() {
        return (
            <React.Fragment>
                <Grid.Row>
                    <Grid.Col sm={6} lg={6}>
                        <StampCard
                            color="green"
                            icon="book"
                            header={"3 Profile"}
                            footer={"You Enrolled"}
                        />
                    </Grid.Col>
                    <Grid.Col sm={6} lg={6}>
                        <StampCard
                            color="yellow"
                            icon="star"
                            header={"4.5 / 5"}
                            footer={"You Earned"}
                        />
                    </Grid.Col>
                </Grid.Row>
            </React.Fragment>
        );
    }
}

export default Summary;
