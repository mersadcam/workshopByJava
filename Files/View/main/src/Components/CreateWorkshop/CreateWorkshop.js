import React from 'react';
import {Form, Avatar, Button, Card, Grid, Header, List, Page, Tag, Text, Dropdown} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./details.json";
import './CreateWorkshop.css'
import axios from "axios"


class Workshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            courses:[],
            name:"",
            teacher:"",
            startTime:"",
            finishTime:"",
            course:"",
            category:"",
            place:"",
            capacity:0,
            value:0,
            description:"",
            msg:""

        }
    }

    componentDidMount(): void {
        const {workshopID} = this.props.match.params;
        axios.get("http://localhost:8000/user/course").then(res=>{
            this.setState({courses:res.data,course:res.data[0].name,category:details.courses[0].name})
        }).catch(e=>{
            console.log(e)
        })
    }

    createWorkshop(){

        const toSend = {
            name:this.state.name,
            teacher:this.state.teacher,
            startTime:this.state.startTime,
            finishTime:this.state.finishTime,
            course:this.state.course,
            category:this.state.category,
            place:this.state.place,
            value:this.state.value,
            description:this.state.description,
            capacity:this.state.capacity
        }

        axios.post("http://localhost:8000/admin/enterNewWorkshop",toSend).then(res=>{
            this.setState({msg:res.data.msg})
            this.props.history.push("/dashboard");

        }).catch(e=>{
            console.log(e)
        })

    }

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row cards>
                        <Grid.Col lg={5}>
                            <Card>
                                <img
                                    alt={"Cover"}
                                    src={'/banner/default.jpg'}/>

                                <Card.Header>
                                    <Card.Title>
                                        <Header.H3 className={'text-weight-light'}>
                                            <Form.Input
                                                className={'text-large border-none'}
                                                name="workshopName"
                                                placeholder="Workshop Name..."

                                                onChange={e=>{
                                                    this.setState({name:e.target.value})

                                                }}

                                            />
                                            <Form.Input
                                                className={'border-none'}
                                                name="teacher"
                                                placeholder="Teacher Username..."

                                                onChange={e=>{
                                                    this.setState({teacher:e.target.value})
                                                }}

                                            />
                                        </Header.H3>
                                    </Card.Title>

                                    <Card.Options>
                                        <Button color="dark" icon="upload">Upload Photo</Button>
                                    </Card.Options>
                                </Card.Header>
                            </Card>
                        </Grid.Col>


                        <Grid.Col>
                            <Card>
                                <Card.Body>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <Form.Select label={'Course'}

                                                         onChange={e=>{
                                                             this.setState({course:e.target.value})
                                                         }}
                                            >
                                                {this.state.courses.map((item) => (
                                                    <option> {item.name} </option>
                                                ))}
                                            </Form.Select>

                                            <Form.Input
                                                label={'Start Time'}
                                                name="startTime"
                                                placeholder="Format: HH:MM-DD-MM-YYYY"
                                                onChange={e=>{
                                                    this.setState({startTime:e.target.value})
                                                }}
                                            />

                                            <Form.Input
                                                label={'Finish Time'}
                                                name="finishTime"
                                                placeholder="Format: HH:MM-DD-MM-YYYY"

                                                onChange={e=>{
                                                    this.setState({finishTime:e.target.value})
                                                }}

                                            />
                                            <Form.Input
                                                label={'Capacity'}
                                                name="capacity"

                                                onChange={e=>{
                                                    this.setState({capacity:e.target.value})
                                                }}

                                            />
                                        </Grid.Col>
                                        <Grid.Col>
                                            <Form.Select label={'Category'}

                                                         onChange={e=>{
                                                             this.setState({category:e.target.value})
                                                         }}
                                            >
                                                {details.courses.map((item) => (
                                                    <option> {item.category} </option>
                                                ))}
                                            </Form.Select>
                                            <Form.Input
                                                label={'Place'}
                                                name="place"
                                                placeholder="ex: Mollasadra,Shiraz"


                                                onChange={e=>{
                                                    this.setState({place:e.target.value})
                                                }}
                                            />

                                            <Form.Group label="Price">
                                                <Form.InputGroup>
                                                    <Form.InputGroupPrepend>
                                                        <Form.InputGroupText>
                                                            $
                                                        </Form.InputGroupText>
                                                    </Form.InputGroupPrepend>
                                                    <Form.Input placeholder="ex: 120"

                                                                onChange={e=>{
                                                                    this.setState({value:e.target.value})
                                                                }}
                                                    />
                                                </Form.InputGroup>
                                            </Form.Group>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <Form.Group label={'Description'}>
                                                <Form.Textarea name="description"
                                                               placeholder="Enter Workshop Description..."
                                                               onChange={e=>{
                                                                   this.setState({description:e.target.value})
                                                               }}
                                                />
                                            </Form.Group>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col className={'text-center'}>
                                            <Button icon="check" color="blue"
                                            onClick={e=>this.createWorkshop()}
                                            >Create</Button>
                                            <Button color="secondary" className={'ml-3'} RootComponent={'a'} href={'/dashboard'}>Cancel</Button>
                                        </Grid.Col>
                                    </Grid.Row>
                                </Card.Body>
                            </Card>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
            ;
    }
}

export default Workshop;
