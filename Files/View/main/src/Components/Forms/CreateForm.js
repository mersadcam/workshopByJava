import React from 'react';
import {Card, Grid, Page, Form, Button, Icon} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios"

class CreateForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            name:"",
            form:{},
            questionNumber:1,
            workshopId:""
        }
    }

    componentWillMount(): void {
        const {workshopID} = this.props.match.params;
        console.log(workshopID)
        this.setState({workshopId:workshopID})
    }

    addQuestion = () => {
        this.setState({questionNumber:this.state.questionNumber+1})
    };

    sendForm(){
        const toSend={
            workshopId:this.state.workshopId,
            formBody:this.state.form,
            name:this.state.name
        }


        axios.post("http://localhost:8000/user/workshop/newForm",toSend).then(
            res=>{

                this.props.history.push("/rootworkshop/"+this.state.workshopId)
                console.log(res)
            }
        ).catch(e=>{
            console.log(e)
        })

    }

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
                                <Grid.Col lg={3}>
                                    <Form.Input
                                        label={'Form Name :'}
                                        name="formName"
                                        placeholder={"Form Name "}
                                        onChange={e=>{
                                            this.setState({name:e.target.value})
                                        }}

                                    />
                                </Grid.Col>

                            </Grid.Row>
                            <Grid.Row>



                                <Grid.Col>
                                    <Form.Group label={"Form Body :"}>
                                    {[...Array(this.state.questionNumber).keys()].map((item) => (

                                            <Form.InputGroup>
                                                <Form.InputGroupPrepend>
                                                    <Form.InputGroupText>
                                                        {item+1}
                                                    </Form.InputGroupText>
                                                </Form.InputGroupPrepend>
                                                <Form.Input placeholder="Enter Question..."
                                                onChange={e=>{
                                                    this.setState({form:{...this.state.form,[item]:e.target.value}})
                                                }}
                                                />
                                            </Form.InputGroup>

                                    ))}
                                    </Form.Group>
                                </Grid.Col>
                            </Grid.Row>
                            <Grid.Row className={'text-center'}>
                                <Grid.Col>
                                    <Button icon='check' type='submit' color='blue' onClick={e=>this.sendForm()} >Create</Button>
                                    <Button color='secondary' className={'ml-2'}
                                            RootComponent={'a'}
                                            href={"/rootworkshop/"+this.state.workshopId}
                                    >Cancel</Button>
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
