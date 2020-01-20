import React from 'react';
import {Card, Grid, Page, Form, Button, Avatar, Icon, Header, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import json from "./Workshops.json";
import Search from "./Search";

class Workshops extends React.Component {

    options =
        <React.Fragment>
            <Form.Select className="w-auto mr-2">
                <option value="asc">Newest</option>
                <option value="desc">Oldest</option>
            </Form.Select>
            <Form.Select className="w-auto mr-2">
                <option value="asc">All Places</option>
                <option value="desc">Tehran</option>
                <option value="desc">Shiraz</option>
                <option value="desc">Yazd</option>
                <option value="desc">Tabriz</option>
                <option value="desc">Isfahan</option>
            </Form.Select>
            <Search/>
        </React.Fragment>;

    render() {
        return (
            <SiteTemplate>
                <Page.Content>
                    <Page.Header
                        title="Explore Workshops"
                        options={this.options}
                    />
                    <Grid.Row>
                        {json.items.map((item, key) => (
                            <Grid.Col md={6} lg={4} xl={3} key={key}>
                                <img
                                    alt={item.title}
                                    src={item.imageURL}
                                />
                                <Card>
                                    <Card.Body>
                                        <Grid.Row>
                                            <Grid.Col width={10}>
                                                <Header.H4><a href={'#'}>{item.title}</a></Header.H4>
                                            </Grid.Col>
                                            <Grid.Col width={2}>
                                                <a href={'#'}><FontAwesomeIcon size='sm' color={'gray'} icon={['far', 'bookmark']}/></a>
                                            </Grid.Col>
                                        </Grid.Row>
                                        <Text color={'gray'}>
                                            <FontAwesomeIcon size='sm' icon={"map-marker-alt"}/>
                                            <Text.Small className={'mx-2'}> {item.place} </Text.Small>
                                            <FontAwesomeIcon size='sm' icon={"calendar-check"}/>
                                            <Text.Small className={'mx-2'}> {item.date} </Text.Small>
                                            <FontAwesomeIcon size='sm' icon={"credit-card"}/>
                                            <Text.Small className={'mx-2'}> {item.price} </Text.Small>
                                        </Text>
                                        <Grid.Row className={'mt-3'}>
                                            <Grid.Col lg={1} md={1} sm={1}>
                                                <Avatar size="sm" imageURL={item.avatarURL}/>
                                            </Grid.Col>
                                            <Grid.Col lg={6} md={3} sm={3}>
                                                <a className={'text-inherit text-nowrap text-gray mx-2'} href={'#'}>{item.teacher}</a>
                                            </Grid.Col>
                                            <Grid.Col lg={2} md={10} sm={10}>
                                                <Button color="primary" size="sm">Enroll</Button>
                                            </Grid.Col>
                                        </Grid.Row>
                                    </Card.Body>
                                </Card>
                            </Grid.Col>
                        ))}
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default Workshops;
