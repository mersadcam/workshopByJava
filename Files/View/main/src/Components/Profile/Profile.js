import React from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'

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
                                                    {profile.userType !== "user" && <Tag color={'blue'}> {profile.userType} </Tag>}
                                                    <Button icon={'edit'} size={'sm'} className={'ml-3 px-3'}> Edit </Button>
                                                </Text>
                                            </Grid.Row>

                                            <Grid.Row>
                                                <Header.H4 className={'text-weight-light'}>{profile.subtitle} </Header.H4>
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
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default Profile;
