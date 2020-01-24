import React from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag, Avatar,List} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import Carousel from "../Carousel/Carousel";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'
import WorkshopCard from "../WokshopCard/WorkshopCard";
import GifLoader from 'react-gif-loader';
import enrolledWokshops from "./EnrolledWorkshops";
import gradingWokshops from "./GradingWorkshops";
import teachingWorkshops from "./TeachingWorkshops";
import axios from "axios";

class Profile extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            username:"kaji78",
            loading:true,
            workshops:{},
            user:{},
            contactPoint:{}
        }
    }

    async componentDidMount(): void {

        await axios.post("http://localhost:8000/user/profile",{username:this.state.username})
            .then(res=>{
                this.setState({workshops:res.data.body.workshops,
                    contactPoint:res.data.body.contactPoint,
                    user:res.data.body.user,
                    loading:false})

                console.log(this.state.workshops)


            }).catch(e=>{
                console.log(e)
            })

    }


    render() {
        const {user,loading,workshops,contactPoint} = this.state;

        if(loading){


            return (
                <GifLoader
                    loading={true}
                    imageSrc="https://thumbs.gfycat.com/LameDifferentBalloonfish-small.gif"
                    imageStyle={{marginTop:"10%"}}
                    overlayBackground="rgba(0,0,0,0.5)"
                />
            );

        }else{

            return (
                <SiteTemplate>
                    <Page.Content>
                        <img
                            alt={user.username + " Cover"}
                            src={profile.coverURL}
                        />
                        <Grid.Row className={'justify-content-center'}>
                            <Grid.Col>
                                <Card>
                                    <Card.Body>
                                        <Grid.Row alignItems={'center'}>
                                            <Grid.Col lg={2} className={'text-center'}>
                                                <img
                                                    alt={user.username + " Cover"}
                                                    src={profile.avatarURL}
                                                    className={'rounded-circle avatar-big'}
                                                />
                                            </Grid.Col>
                                            <Grid.Col lg={10}>
                                                <List unstyled seperated>
                                                    <List.Item>
                                                        <h3 className={'d-inline'}> {user.username} </h3>
                                                        <Text transform={'uppercase'} className={'d-inline ml-3'}>
                                                            {user.userType !== "user" &&
                                                            <Tag color={'blue'}> {user.userType} </Tag>}
                                                        </Text>
                                                    </List.Item>
                                                    <List.Item className={'mt-3'}>
                                                        <Header.H4 className={'text-weight-light'}>{contactPoint.subTitle} </Header.H4>
                                                    </List.Item>
                                                    <List.Item className={'mt-3'}> <b className={'mr-2'}> Name </b> {contactPoint.fullName} </List.Item>
                                                    <List.Item className={'mt-3'}> <b className={'mr-2'}> Email </b> {contactPoint.emailAddress} </List.Item>
                                                    <List.Item className={'mt-3'}> <b className={'mr-2'}> Bio </b> {contactPoint.biography} </List.Item>
                                                    <List.Item className={'mt-5'}>
                                                        <Button.List>
                                                            <Button size={'sm'} color={'secondary'} icon={'edit'} RootComponent={'a'} href={'/editprofile'}> Edit </Button>
                                                            <Button size={'sm'} color={'secondary'}>Send Message</Button>
                                                            <Button size={'sm'} color={'secondary'}>Report</Button>
                                                        </Button.List>
                                                    </List.Item>
                                                </List>
                                            </Grid.Col>
                                        </Grid.Row>
                                    </Card.Body>
                                </Card>
                            </Grid.Col>
                        </Grid.Row>

                        <Card>
                            <Card.Header>
                                <Card.Title> Teaching Workshops </Card.Title>
                            </Card.Header>
                            <Carousel>
                                {workshops.map((item, key) => (
                                    <WorkshopCard
                                        title={item.workshop.name}
                                        imageURL={item.workshop.imageURL}
                                        avatarURL={item.workshop.avatarURL}
                                        teacher={item.workshop.teacher}
                                        date={item.workshop.startTime}
                                        place={item.workshop.place}
                                        price={item.workshop.value}
                                        buttonText="View"
                                        buttonColor="secondary"/>
                                ))}
                            </Carousel>
                        </Card>

                        <Card>
                            <Card.Header>
                                <Card.Title> Grading Workshops </Card.Title>
                            </Card.Header>
                            <Carousel>
                                {gradingWokshops.items.map((item, key) => (
                                    <WorkshopCard
                                        title={item.title}
                                        imageURL={item.imageURL}
                                        avatarURL={item.avatarURL}
                                        teacher={item.teacher}
                                        date={item.date}
                                        place={item.place}
                                        price={item.price}
                                        buttonText="View"
                                        buttonColor="secondary"/>
                                ))}
                            </Carousel>
                        </Card>

                        <Card>
                            <Card.Header>
                                <Card.Title> Enrolled Workshops </Card.Title>
                            </Card.Header>
                            <Carousel>
                                {enrolledWokshops.items.map((item, key) => (
                                    <WorkshopCard
                                        title={item.title}
                                        imageURL={item.imageURL}
                                        avatarURL={item.avatarURL}
                                        teacher={item.teacher}
                                        date={item.date}
                                        place={item.place}
                                        price={item.price}
                                        buttonText="View"
                                        buttonColor="secondary"/>
                                ))}
                            </Carousel>
                        </Card>


                    </Page.Content>
                </SiteTemplate>
            )

        }
    }
}

export default Profile;
