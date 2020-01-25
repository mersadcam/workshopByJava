import React from 'react';
import {Button, Card, Table} from 'tabler-react';
import enrolledWorkshops from "./EnrolledWorkshops.json";

class MyWorkshops extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            workshopsNumber : enrolledWorkshops.items.length
        }
    }
    render() {

        const {workshops} = this.props

        return (
            <Card title={"My Workshops (" + this.state.workshopsNumber + ")"} className={'px-2'}>
                <Table responsive>
                    <Table.Header>
                        <Table.ColHeader>Workshop</Table.ColHeader>
                        <Table.ColHeader>Teacher</Table.ColHeader>
                        <Table.ColHeader>Start</Table.ColHeader>
                        <Table.ColHeader>{null}</Table.ColHeader>
                    </Table.Header>
                    <Table.Body>
                        {workshops.map(item => (
                            <Table.Row>
                                <Table.Col><a href={'?'}>{item.workshop.name}</a></Table.Col>
                                <Table.Col>{item.teacher.fullName}</Table.Col>
                                <Table.Col>{item.workshop.startTime}</Table.Col>
                                <Table.Col><Button outline size="sm" color="primary"> Manage </Button></Table.Col>
                            </Table.Row>
                        ))}
                    </Table.Body>
                </Table>
            </Card>
        );
    }
}

export default MyWorkshops;
