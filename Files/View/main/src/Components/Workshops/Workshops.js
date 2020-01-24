import React from 'react';
import {Card, Grid, Page, Form} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import json from "./Workshops.json";
import Search from "./Search";
import WorkshopCard from "../WokshopCard/WorkshopCard";
import axios from "axios"

class Workshops extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            workshops:[]

        }
    }

    async componentDidMount(): void {

        await axios.get("http://localhost:8000/user/workshops").then(res=>{
            console.log(res.data)

            this.setState({workshops:res.data.body})
        }).catch(e=>{
            console.log(e)
        })

    }


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

        const {workshops} = this.state

        return (
            <SiteTemplate>
                <Page.Content>
                    <Page.Header
                        title="Explore Workshops"
                        options={this.options}
                    />
                    <Grid.Row>
                        {workshops.map((item) => (
                            <Grid.Col md={6} lg={4} xl={3}>
                                <WorkshopCard
                                    title={item.workshop.name}
                                    imageURL={item.imageURL}
                                    avatarURL={item.avatarURL}
                                    teacher={item.teacher.fullName}
                                    teacherUsername={item.teacher.username}
                                    date={item.workshop.startTime}
                                    place={item.workshop.place}
                                    price={item.workshop.value}
                                    buttonText="View"
                                    buttonURL={'/workshop'}
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
