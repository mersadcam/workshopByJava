import React from 'react';
import {Card, Grid, Page, Form, Button, Stamp} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios"

class ViewForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            form: [
                {number: 1, question: "question one?", answer: "answer one"},
                {number: 2, question: "question two?", answer: "answer two"},
                {number: 3, question: "question three?", answer: "answer two"},
            ]
        }
    }

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    {this.state.form.map((item) => (
                        <Card>
                            <Card.Status color="blue" side />
                            <Card.Header>
                                <Card.Title>
                                    {item.question}
                                </Card.Title>
                            </Card.Header>
                            <Card.Body>
                                    {item.answer}
                            </Card.Body>
                        </Card>
                    ))}
                    <Grid.Row className={'text-center'}>
                        <Grid.Col>
                            <Button color='blue'>Go Back</Button>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default ViewForm;
