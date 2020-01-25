import React from 'react';
import {Form, Avatar, Button, Card, Grid, Header, List, Page, Tag, Text, Dropdown} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./details.json";
import './CreateWorkshop.css'


class Workshop extends React.Component {
    constructor(props) {
        super(props);
        this.state = {}
    }

    componentDidMount(): void {
        const {workshopID} = this.props.match.params;
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
                    <Grid.Row cards>
                        <Grid.Col lg={5}>
                            <Card>
                                <img
                                    alt={"Cover"}
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
                                            <Form.Select label={'Course'}>
                                                {details.courses.map((item) => (
                                                    <option> {item.name} </option>
                                                ))}
                                            </Form.Select>

                                            <Form.Input
                                                label={'Start Time'}
                                                name="startTime"
                                                placeholder="Format: HH:MM-DD-MM-YYYY"
                                            />

                                            <Form.Input
                                                label={'Finish Time'}
                                                name="finishTime"
                                                placeholder="Format: HH:MM-DD-MM-YYYY"
                                            />
                                        </Grid.Col>
                                        <Grid.Col>
                                            <Form.Select label={'Category'}>
                                                {details.courses.map((item) => (
                                                    <option> {item.category} </option>
                                                ))}
                                            </Form.Select>
                                            <Form.Input
                                                label={'Place'}
                                                name="place"
                                                placeholder="ex: Mollasadra,Shiraz"
                                            />

                                            <Form.Group label="Price">
                                                <Form.InputGroup>
                                                    <Form.InputGroupPrepend>
                                                        <Form.InputGroupText>
                                                            $
                                                        </Form.InputGroupText>
                                                    </Form.InputGroupPrepend>
                                                    <Form.Input placeholder="ex: 120"/>
                                                </Form.InputGroup>
                                            </Form.Group>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <Form.Group label={'Description'}>
                                                <Form.Textarea name="description"
                                                               placeholder="Enter Workshop Description..."/>
                                            </Form.Group>
                                        </Grid.Col>
                                    </Grid.Row>
                                    <Grid.Row>
                                        <Grid.Col className={'text-center'}>
                                            <Button icon="check" color="blue">Create</Button>
                                            <Button color="secondary" className={'ml-3'}>Cancel</Button>
                                        </Grid.Col>
                                    </Grid.Row>
                                </Card.Body>
                            </Card>
                        </Grid.Col>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        )
            ;
    }
}

export default Workshop;
