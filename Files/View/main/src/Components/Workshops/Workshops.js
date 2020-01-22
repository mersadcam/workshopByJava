import React from 'react';
import {Card, Grid, Page, Form, Button, Avatar, Icon, Header, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import json from "./Workshops.json";
import Search from "./Search";
import WorkshopCard from "../WokshopCard/WorkshopCard";

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
                                <WorkshopCard
                                    title={item.title}
                                    imageURL={item.imageURL}
                                    avatarURL={item.avatarURL}
                                    teacher={item.teacher}
                                    date={item.date}
                                    place={item.place}
                                    price={item.price}
                                    buttonText="View"
                                    buttonColor="primary"/>
                            </Grid.Col>
                        ))}
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
    }
}

export default Workshops;
