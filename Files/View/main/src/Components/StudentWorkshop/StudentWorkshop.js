import React from 'react';
import {Avatar, Button, Card, Grid, Header, List, Page, Stamp, Table, Tag, Text} from "tabler-react";
import SiteTemplate from "../../SiteTemplate";
import details from "./details.json";


class StudentWorkshop extends React.Component {
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
                    <Grid.Row cards deck>
                        <Grid.Col lg={5}>
                            <Card>
                                <img
                                    alt={details.workshop.name + " Cover"}
                                    src={'/demo/photos/illustrator.jpg'}/>
                                <Card.Header>
                                    <Card.Title>
                                        <Header.H3
                                            className={'text-weight-light'}>{details.workshop.name}</Header.H3>
                                        <Tag className={'mr-2'}> #{details.workshop.category}</Tag>
                                        <Tag className={'mr-2'}> #{details.workshop.course}</Tag>
                                    </Card.Title>

                                    <Card.Options className={'mt-3'}>
                                        <Grid.Col className={'text-center'}>

                                            {details.report.studentCourseStatus !== null ?
                                                details.report.studentCourseStatus === "PASSED" ?
                                                    <div>
                                                        <Stamp className={'px-5 text-large text-weight-light'}
                                                               color={'green'}>{details.report.finalNumber}/{details.report.completeNumber}</Stamp>
                                                        <h4 className={'text-weight-light text-green mt-2'}>PASSED</h4>
                                                    </div>
                                                    :
                                                    <div>
                                                        <Stamp className={'px-5 text-large text-weight-light'}
                                                               color={'red'}>{details.report.finalNumber}/{details.report.completeNumber}</Stamp>
                                                        <h4 className={'text-weight-light text-red mt-2'}>NOT
                                                            PASSED</h4>
                                                    </div>
                                                :
                                                <div>
                                                    <Stamp className={'px-5 text-large text-weight-light'}
                                                           color={'blue'}>Enrolled</Stamp>
                                                    <h4 className={'text-weight-light mt-2'}>LEARNING</h4>
                                                </div>
                                            }
                                        </Grid.Col>

                                    </Card.Options>
                                </Card.Header>
                            </Card>
                        </Grid.Col>
                        <Grid.Col>
                            <Card>
                                <Card.Header>
                                    <Avatar imageURL={'/demo/faces/male/33.jpg'}/>
                                    <a className={'text-inherit mx-2'}
                                       href={"/profile/" + details.teacher.username}><b
                                        className={'mr-2'}> Teacher </b> {details.teacher.fullName}</a>
                                </Card.Header>
                                <Card.Body>
                                    <Grid.Row>
                                        <Grid.Col>
                                            <List unstyled seperated>
                                                <List.Item> <b className={'mr-2'}> Start Time </b> {this.timeFormat(details.workshop.startTime)} </List.Item>
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
                        <Card title={"Forms (" + details.forms.length + ")"} className={'px-2'}>
                            <Table responsive>
                                <Table.Header>
                                    <Table.ColHeader>Writer</Table.ColHeader>
                                    <Table.ColHeader>Date</Table.ColHeader>
                                    <Table.ColHeader>{null}</Table.ColHeader>
                                </Table.Header>
                                <Table.Body>
                                    {details.forms.map((item) => (
                                        <Table.Row>
                                            <Table.Col>{item.writer}</Table.Col>
                                            <Table.Col>{this.timeFormat(item.date)}</Table.Col>
                                            <Table.Col><Button outline size="sm" color="primary"> Read </Button></Table.Col>
                                        </Table.Row>
                                    ))}
                                </Table.Body>
                            </Table>
                        </Card>
                    </Grid.Row>
                </Page.Content>
            </SiteTemplate>
        );
    }
}

export default StudentWorkshop;
