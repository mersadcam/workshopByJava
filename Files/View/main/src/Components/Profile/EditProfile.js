import React,{useCallback} from 'react';
import {Card, Grid, Page, Button, Header, Text, Tag, Avatar, Form} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import profile from "./Profile.json";
import './Profile.css'

class Profile extends React.Component {
    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Form onSubmit={(event) => console.log(event.target.name + 'clicked')}>
                        <img alt={profile.username + " Cover"} src={profile.coverURL}/>

                        <Card>
                            <Card.Body>
                                <Grid.Row alignItems={'center'}>
                                    <Grid.Col lg={3} className={'text-center'}>
                                        <img
                                            alt={profile.username + " Cover"}
                                            src={profile.avatarURL}
                                            className={'rounded-circle avatar-big'}/>
                                    </Grid.Col>


                                    <Grid.Col lg={9}>
                                        <Grid.Row>
                                            <Grid.Col lg={3}>
                                                <Form.Group isRequired label="Username"> <Form.Input
                                                    value={profile.username} name="username"/> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={9}>
                                                <Form.Group label="Subtitle"> <Form.Input
                                                    value={profile.subtitle} name="subtitle"/> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col lg={3}>
                                                <Form.Group isRequired label="First Name"> <Form.Input
                                                    value={profile.firstName} name="firstName"/> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={3}>
                                                <Form.Group isRequired label="Last Name"> <Form.Input
                                                    value={profile.lastName} name="lastName"/> </Form.Group>
                                            </Grid.Col>
                                            <Grid.Col lg={6}>
                                                <Form.Group isRequired label="Email"> <Form.Input
                                                    value={profile.email} name="email"/> </Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>


                                        <Grid.Row>
                                            <Grid.Col>
                                                <Form.Group label={'Bio'}> <Form.Textarea
                                                    defaultValue={profile.bio} name="bio"/></Form.Group>
                                            </Grid.Col>
                                        </Grid.Row>

                                        <Grid.Row className={'justify-content-center'}>
                                            <Grid.Col lg={4} >
                                                <Button type='submit' color='blue'>Apply</Button>
                                                <Button color='secondary' className={'ml-2'}>Cancel</Button>
                                            </Grid.Col>
                                        </Grid.Row>

                                    </Grid.Col>
                                </Grid.Row>
                            </Card.Body>
                        </Card>
                    </Form>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default Profile;
