import React from 'react';
import {Avatar, Button, Card, Grid, Header, List, Page, Tag, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import axios from "axios";


class Workshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            teacher: {},
            workshop: {},
            role: "",
            startTime:"",
            finishTime:""
        }
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
    }

    enroll(){

        const toSend = {workshopId: this.state.workshop._id,
        course:this.state.workshop.course,
        paymentType:"cash"}

        axios.post("http://localhost:8000/user/workshop/studentRequest",toSend).then(res=>{
            this.props.history.push("/dashboard");
        }).catch(e=>console.log(e))

    }

    graderRequest(){

        const toSend = {
            enteredCourseId:this.state.workshop._id
        }

        axios.post("http://localhost:8000/user/workshop/graderRequest",toSend).then(
            res=>{
                if(res.data.status){
                    this.props.history.push("/dashboard");
                }
            }
        ).catch(e=>console.log(e))

    }

    timeFormat = (timePattern) => {
        console.log(timePattern)
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
                                    alt={"Cover"}
                                    src={'/banner/default.jpg'}/>
                                <Card.Header>
                                    <Card.Title>
                                        <Header.H3
                                            className={'text-weight-light'}>{this.state.workshop.name}</Header.H3>
                                        <Tag className={'mr-2'}> #{this.state.workshop.category}</Tag>
                                        <Tag className={'mr-2'}> #{this.state.workshop.course}</Tag>
                                    </Card.Title>

                                    <Card.Options className={'mt-3'}>
                                        <Grid.Col>
                                            <Grid.Row>
                                                <Grid.Col>
                                                    <Button className={'px-7 text-large'}
                                                            color={'primary'}
                                                    onClick={e=>this.enroll()}> Enroll </Button>
                                                </Grid.Col>
                                            </Grid.Row>
                                            <Grid.Row>
                                                <Grid.Col className={'text-center'}>
                                                    <h3 className={'text-dark mt-3'}> {this.state.workshop.value} $ </h3>
                                                </Grid.Col>
                                            </Grid.Row>
                                        </Grid.Col>
                                    </Card.Options>
                                </Card.Header>
                            </Card>
                        </Grid.Col>
                        <Grid.Col>
                            <Card>
                                <Card.Header>
                                    <Avatar imageURL={'/default-avatar.png'}/>
                                    <a className={'text-inherit mx-2'}
                                       href={"/profile/" + this.state.teacher.username}><b
                                        className={'mr-2'}> Teacher </b> {this.state.teacher.fullName}</a>
                                    <Card.Options>
                                        <Button outline color={'primary'}
                                        onClick={e=>this.graderRequest()}
                                        > Grading Request </Button>
                                    </Card.Options>
                                </Card.Header>
                                <Card.Body>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b className={'mr-2'}> Start
                                                    Time </b> {
                                                    this.timeFormat(this.state.startTime)}
                                                </List.Item>
                                                <List.Item className={'mt-5'}> <b className={'mr-2'}>
                                                    Finish Time </b> {this.timeFormat(this.state.finishTime)}
                                                </List.Item>
                                                <List.Item className={'mt-5'}> <b
                                                    className={'mr-2'}> Place </b> {this.state.workshop.place}
                                                </List.Item>

                                            </List>
                                        </Grid.Col>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b
                                                    className={'mr-2'}> Price </b> {this.state.workshop.value} $
                                                </List.Item>
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
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default Workshop;
