import React from 'react';
import {Card, Grid, Page, Form} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import json from "./Workshops.json";
import Search from "./Search";
import WorkshopCard from "../WokshopCard/WorkshopCard";

class Workshops extends React.Component {

    options =
        <React.Fragment>
            <Form.Select className="w-auto mr-2">
                <option>Newest</option>
                <option>Oldest</option>
            </Form.Select>
            <Form.Select className="w-auto mr-2">
                <option>All Places</option>
                <option>Tehran</option>
                <option>Shiraz</option>
                <option>Yazd</option>
                <option>Tabriz</option>
                <option>Isfahan</option>
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
                                    id={item.id}
                                    imageURL={item.imageURL}
                                    avatarURL={item.avatarURL}
                                    teacher={item.teacher}
                                    teacherUsername={item.teacherUsername}
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
