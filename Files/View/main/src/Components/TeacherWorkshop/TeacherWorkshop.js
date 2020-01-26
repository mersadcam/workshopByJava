import React from 'react';
import {Avatar, Button, Card, Grid, Header, List, Page, Stamp, Table, Tag, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios";


class TeacherWorkshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allStudentsNumber: 0,
            teacher: {},
            workshop: {},
            role: {},
            startTime:"",
            finishTime:"",
            forms:[],
            graders:[]
        }
    }


    static getDerivedStateFromProps(props, state) {
        return {
            teacher: props.teacher,
            workshop: props.workshop,
            role: props.role,
            startTime: props.startTime,
            finishTime: props.finishTime,
        };
    }
    

    async componentDidMount(): void {

            const toSend = {form:this.state.role.form}
        axios.all([axios.post('http://localhost:8000/user/forms',toSend),
            axios.post('http://localhost:8000/user/grader/request',{workshopId:this.state.workshop._id})])
            .then(axios.spread((firstResponse, secondResponse) => {

                this.setState({forms:firstResponse.data,graders:secondResponse.data})
                console.log("grader",this.state.graders)
                console.log("forms",this.state.forms)

            }))
            .catch(error => console.log(error));


        // let allStudentsNumber = 0;
        // this.state.groups.map((item) => allStudentsNumber += item.identities.length);
        // this.setState({allStudentsNumber:allStudentsNumber});
    }

    timeFormat = (timePattern) => {
        const array = timePattern.split("-");
        const monthArray = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
        const time = array[0];
        const day = array[1];
        const month = monthArray[parseInt(array[2])];
        const year = array[3];
        return time + ' | ' + day + ' ' + month + ' ' + year;
    };

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Grid.Row cards deck>
                        <Grid.Col lg={5}>
                            <Card>
                                <img
                                    alt={this.state.workshop.name + " Cover"}
                                    src={'/banner/default.jpg'}/>
                                <Card.Header>
                                    <Card.Title className={'my-4'}>
                                        <Header.H3
                                            className={'text-weight-light'}>{this.state.workshop.name}</Header.H3>
                                        {/*<Tag className={'mr-2'}> #{this.state.workshop.category}</Tag>*/}
                                        <Tag className={'mr-2'}> #{this.state.workshop.course}</Tag>
                                    </Card.Title>

                                    <Card.Options className={'pr-2'}>
                                        <Button color={'blue'} RootComponent={'a'} href={"/createform/" + this.state.workshop._id}> Create new form </Button>
                                    </Card.Options>
                                </Card.Header>
                            </Card>

                            {/*<Card>*/}
                            {/*    <Card.Header>*/}
                            {/*        <Card.Title className={'my-4'}>*/}
                            {/*            Forms ({this.state.forms.length})*/}
                            {/*        </Card.Title>*/}

                            {/*        <Card.Options className={'pr-2'}>*/}
                            {/*            <Button icon='plus' color={'blue'}> Add Form </Button>*/}
                            {/*        </Card.Options>*/}
                            {/*    </Card.Header>*/}
                            {/*    <Table responsive>*/}
                            {/*        <Table.Body>*/}
                            {/*            {this.state.forms.map((item) => (*/}
                            {/*                <Table.Row>*/}
                            {/*                    <Table.Col>{this.timeFormat(item.date)}</Table.Col>*/}
                            {/*                    <Table.Col>*/}
                            {/*                        <Button icon={'edit'} outline size="sm" color="primary"> Edit </Button>*/}
                            {/*                    </Table.Col>*/}
                            {/*                    <Table.Col>*/}
                            {/*                        <Button icon={'trash'} outline size="sm" color="secondary"> Delete </Button>*/}
                            {/*                    </Table.Col>*/}
                            {/*                </Table.Row>*/}
                            {/*            ))}*/}
                            {/*        </Table.Body>*/}
                            {/*    </Table>*/}
                            {/*</Card>*/}


                        </Grid.Col>
                        <Grid.Col>


                            <Card>
                                <Card.Header>
                                    <Avatar imageURL={'/default-avatar.png'}/>
                                    <a className={'text-inherit mx-2'}
                                       href={"/profile/" + this.state.teacher.username}><b
                                        className={'mr-2'}> Teacher </b> {this.state.teacher.fullName}</a>
                                </Card.Header>
                                <Card.Body>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b className={'mr-2'}> Start Time </b> {this.timeFormat(this.state.workshop.startTime)} </List.Item>
                                                <List.Item className={'mt-5'}> <b className={'mr-2'}>
                                                    Finish Time </b> {this.timeFormat(this.state.workshop.finishTime)} </List.Item>
                                                <List.Item className={'mt-5'}> <b
                                                    className={'mr-2'}> Place </b> {this.state.workshop.place} </List.Item>

                                            </List>
                                        </Grid.Col>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b
                                                    className={'mr-2'}> Price </b> {this.state.workshop.value} $ </List.Item>
                                                <List.Item className={'mt-5'}> <b className={'mr-2'}>
                                                    Capacity </b> {this.state.workshop.capacity} </List.Item>
                                            </List>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col className={'line-height-larger'}>
                                            <b className={'mr-2'}> Description </b> {this.state.workshop.description}
                                        </Grid.Col>
                                    </Grid.Row>
                                </Card.Body>
                            </Card>




                            {/*<Card>*/}
                            {/*    <Card.Header>*/}
                            {/*        <Card.Title className={'my-4'}>*/}
                            {/*            Groups ({this.state.groups.length})*/}
                            {/*            <Tag color={'blue'} className={'ml-4'}>All Students: {this.state.allStudentsNumber}</Tag>*/}
                            {/*        </Card.Title>*/}

                            {/*        <Card.Options className={'pr-2'}>*/}
                            {/*            <Button icon='plus' color={'blue'}> Add Group </Button>*/}
                            {/*        </Card.Options>*/}
                            {/*    </Card.Header>*/}
                            {/*    <Table responsive>*/}
                            {/*        <Table.Header>*/}
                            {/*            <Table.ColHeader>Name</Table.ColHeader>*/}
                            {/*            <Table.ColHeader>Super Grader</Table.ColHeader>*/}
                            {/*            <Table.ColHeader>Size</Table.ColHeader>*/}
                            {/*            <Table.ColHeader>{null}</Table.ColHeader>*/}
                            {/*        </Table.Header>*/}
                            {/*        <Table.Body>*/}
                            {/*            {this.state.groups.map((item) => (*/}
                            {/*                <Table.Row>*/}
                            {/*                    <Table.Col>{item.name}</Table.Col>*/}
                            {/*                    <Table.Col>{item.superGrader}</Table.Col>*/}
                            {/*                    <Table.Col>{item.identities.length}</Table.Col>*/}
                            {/*                    <Table.Col>*/}
                            {/*                        <Button icon={'edit'} outline size="sm" color="primary"> Edit </Button>*/}
                            {/*                    </Table.Col>*/}
                            {/*                    <Table.Col>*/}
                            {/*                        <Button icon={'trash'} outline size="sm" color="secondary"> Delete </Button>*/}
                            {/*                    </Table.Col>*/}
                            {/*                </Table.Row>*/}
                            {/*            ))}*/}
                            {/*        </Table.Body>*/}
                            {/*    </Table>*/}
                            {/*</Card>*/}

                        </Grid.Col>
                    </Grid.Row>

                    <Grid.Row>

                        <Grid.Col lg={5}>

                            <Card title="My Forms" className={'px-2'}>
                                <Table responsive>
                                    <Table.Header>
                                        <Table.ColHeader>id</Table.ColHeader>
                                        <Table.ColHeader>name</Table.ColHeader>
                                        <Table.ColHeader>{null}</Table.ColHeader>
                                    </Table.Header>
                                    <Table.Body>
                                        {
                                            this.state.forms.map((item,index)=>(

                                                <Table.Row>
                                                    <Table.Col>{index+1}</Table.Col>
                                                    <Table.Col>{item.name}</Table.Col>

                                                    <Table.Col><Button outline size="sm" color="primary" RootComponent={'a'} href={"/viewform/"+item._id+"/"+this.state.workshop._id}> View </Button></Table.Col>
                                                </Table.Row>

                                            ))

                                        }


                                    </Table.Body>
                                </Table>
                            </Card>
                        </Grid.Col>

                        <Grid.Col>

                            <Card title="Grader Requests" className={'px-2'}>
                                <Table responsive>
                                    <Table.Header>
                                        <Table.ColHeader>username</Table.ColHeader>
                                        <Table.ColHeader>full name</Table.ColHeader>
                                        <Table.ColHeader>Date</Table.ColHeader>
                                        <Table.ColHeader>{null}</Table.ColHeader>
                                        <Table.ColHeader>{null}</Table.ColHeader>
                                    </Table.Header>
                                    <Table.Body>
                                        {
                                            this.state.graders.map(item=>(

                                                <Table.Row>
                                                    <Table.Col ><a href={"/profile/"+item.user.username}>{item.user.username}</a></Table.Col>
                                                    <Table.Col>{item.user.fullName}</Table.Col>
                                                    <Table.Col>{item.requestDate}</Table.Col>

                                                    {item.status=="NOT_ACCEPTED"?
                                                    <Table.Col><Button outline size="sm" color="success" onClick={e => this.accept()}> Accept </Button></Table.Col>
                                                    :
                                                    <Table.Col><Button outline size="sm" color="muted"> Accepted </Button></Table.Col>
                                                      }

                                                </Table.Row>

                                            ))

                                        }


                                    </Table.Body>
                                </Table>
                            </Card>
                        </Grid.Col>

                    </Grid.Row>

                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default TeacherWorkshop;
