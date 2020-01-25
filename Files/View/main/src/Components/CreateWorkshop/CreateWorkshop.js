import React from 'react';
import {Form,Avatar, Button, Card, Grid, Header, List, Page, Tag, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./details.json";
import './CreateWorkshop.css'


class Workshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    componentDidMount(): void {
        const {workshopID} = this.props.match.params ;
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
                                    alt={details.workshop.name + " Cover"}
                                    src={'/demo/photos/illustrator.jpg'}/>

                                <Card.Header>
                                    <Card.Title>
                                        <Header.H3 className={'text-weight-light'}>
                                                <Form.Input
                                                    className={'text-large border-none'}
                                                    name="workshopName"
                                                    placeholder="Workshop Name..."
                                                />
                                            <Form.Input
                                                className={'border-none'}
                                                name="teacher"
                                                placeholder="Teacher Username..."
                                                />
                                        </Header.H3>
                                    </Card.Title>

                                    <Card.Options>
                                        <Button color="dark" icon="upload">Upload Photo</Button>
                                    </Card.Options>
                                </Card.Header>
                            </Card>
                        </Grid.Col>


                        <Grid.Col>
                            <Card>
                                <Card.Body>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item>
                                                    <Form.Input
                                                        label={'Start Time'}
                                                        name="startTime"
                                                        placeholder="HH:MM-DD-MM-YYYY"
                                                    />
                                                </List.Item>


                                                <List.Item className={'mt-5'}> <b className={'mr-2'}>
                                                    Finish Time </b> {this.timeFormat(details.workshop.finishTime)} </List.Item>
                                                <List.Item className={'mt-5'}> <b
                                                    className={'mr-2'}> Place </b> {details.workshop.place} </List.Item>

                                            </List>
                                        </Grid.Col>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b
                                                    className={'mr-2'}> Price </b> {details.workshop.value} $ </List.Item>
                                                <List.Item className={'mt-5'}> <b className={'mr-2'}>
                                                    Capacity </b> {details.workshop.capacity} </List.Item>
                                            </List>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col className={'line-height-larger'}>
                                            <b className={'mr-2'}> Description </b> {details.workshop.description}
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
