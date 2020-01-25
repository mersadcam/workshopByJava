import React from 'react';
import {Card, Grid, Page, Form, Button, Icon} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios"

class CreateForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            form: [
                {number: 1, question: ""}
            ]
        }
    }

    addQuestion = () => {
        this.setState({questions: [...this.state.questions, {number: this.state.questions.length + 1, text: ""}]});
    };

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Card>
                        <Card.Header>
                            <Card.Title>
                                Create New Form
                            </Card.Title>
                            <Card.Options>
                                <Button icon={'plus'} color={'blue'} onClick={this.addQuestion}> Add Question </Button>
                            </Card.Options>
                        </Card.Header>
                        <Card.Body>
                            <Grid.Row>
                                <Grid.Col>
                                    {this.state.form.map((item) => (
                                        <Form.Group>
                                            <Form.InputGroup>
                                                <Form.InputGroupPrepend>
                                                    <Form.InputGroupText>
                                                        {item.number}
                                                    </Form.InputGroupText>
                                                </Form.InputGroupPrepend>
                                                <Form.Input placeholder="Enter Question..."/>
                                            </Form.InputGroup>
                                        </Form.Group>
                                    ))}
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row className={'text-center'}>
                                <Grid.Col>
                                    <Button icon='check' type='submit' color='blue'>Create</Button>
                                    <Button color='secondary' className={'ml-2'}>Cancel</Button>
                                </Grid.Col>
                            </Grid.Row>
                        </Card.Body>
                    </Card>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default CreateForm;
