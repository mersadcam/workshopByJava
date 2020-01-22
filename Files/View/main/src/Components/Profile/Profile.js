import React from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag, Avatar} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import Carousel from "../Carousel/Carousel";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'
import WorkshopCard from "../WokshopCard/WorkshopCard";

import enrolledWokshops from "./EnrolledWorkshops";
import gradingWokshops from "./GradingWorkshops";
import teachingWorkshops from "./TeachingWorkshops";

class Profile extends React.Component {
    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <img
                        alt={profile.username + " Cover"}
                        src={profile.coverURL}
                    />
                    <Grid.Row className={'justify-content-center'}>
                        <Grid.Col>
                            <Card>
                                <Card.Body>
                                    <Grid.Row alignItems={'center'}>
                                        <Grid.Col lg={3} className={'text-center'}>
                                            <img
                                                alt={profile.username + " Cover"}
                                                src={profile.avatarURL}
                                                className={'rounded-circle avatar-big'}
                                            />
                                        </Grid.Col>
                                        <Grid.Col lg={9}>
                                            <Grid.Row>
                                                <h3> {profile.username} </h3>
                                                <Text transform={'uppercase d-inline ml-3'}>
                                                    {profile.userType !== "user" &&
                                                    <Tag color={'blue'}> {profile.userType} </Tag>}
                                                    <Button color={'secondary'} size={'sm'} className={'ml-3 px-3'}>
                                                        <FontAwesomeIcon size={'sm'} icon={'edit'}/> Edit </Button>
                                                </Text>
                                            </Grid.Row>

                                            <Grid.Row>
                                                <Header.H4
                                                    className={'text-weight-light'}>{profile.subtitle} </Header.H4>
                                            </Grid.Row>

                                            <Grid.Row className={'mt-3'}>
                                                <Grid.Col>
                                                    <b className={'mr-2'}> First Name </b> {profile.firstName}
                                                </Grid.Col>
                                                <Grid.Col>
                                                    <b className={'mr-2'}> Email </b> {profile.email}
                                                </Grid.Col>
                                            </Grid.Row>
                                            <Grid.Row className={'mt-3'}>
                                                <Grid.Col>
                                                    <b className={'mr-2'}> Last Name </b> {profile.lastName}
                                                </Grid.Col>
                                                <Grid.Col>
                                                    <b className={'mr-2'}> Gender </b> {profile.gender}
                                                </Grid.Col>
                                            </Grid.Row>
                                            <Grid.Row className={'mt-3'}>
                                                <Grid.Col>
                                                    <b className={'mr-2'}> Bio </b> {profile.bio}
                                                </Grid.Col>
                                            </Grid.Row>
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
                            {teachingWorkshops.items.map((item, key) => (
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

export default Profile;
