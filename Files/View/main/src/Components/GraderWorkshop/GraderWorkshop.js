import React from 'react';
import {Avatar, Button, Card, Grid, Header, List, Page, Stamp, Table, Tag, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios";


class GraderWorkshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            allStudentsNumber: 0,
            teacher: {},
            workshop: {},
            role: {},
            startTime:"",
            finishTime:""
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
    

    async componentWillMount(): void {
        const {workshopID} = this.props.match.params;
        const toSend = {workshopId:workshopID};
        console.log(toSend);
        await axios.post("http://localhost:8000/user/workshop/page", toSend).then(res => {

            this.setState({teacher: res.data.body.teacher});
            this.setState({workshop: res.data.body.workshop,startTime:res.data.body.workshop.startTime,finishTime:res.data.body.workshop.startTime});
            this.setState({role: res.data.body.role});
        }).catch(e => {
            console.log(e)
        })


        let allStudentsNumber = 0;
        this.state.groups.map((item) => allStudentsNumber += item.identities.length);
        this.setState({allStudentsNumber:allStudentsNumber});
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
                                        <Stamp color={'green'}> Grading Successful! </Stamp>
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
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default GraderWorkshop;
