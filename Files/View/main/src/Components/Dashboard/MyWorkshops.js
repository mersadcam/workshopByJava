import React from 'react';
import {Button, Card, Table} from 'tabler-react';
import enrolledWorkshops from "./EnrolledWorkshops.json";

class MyWorkshops extends React.Component {

    render() {
        return (
            <Card title="My Workshops" className={'px-2'}>
                <Table responsive>
                    <Table.Header>
                        <Table.ColHeader>Workshop</Table.ColHeader>
                        <Table.ColHeader>Teacher</Table.ColHeader>
                        <Table.ColHeader>Start</Table.ColHeader>
                        <Table.ColHeader>{null}</Table.ColHeader>
                    </Table.Header>
                    <Table.Body>
                        {enrolledWorkshops.items.map((item, key) => (
                            <Table.Row>
                                <Table.Col><a href={'?'}>{item.title}</a></Table.Col>
                                <Table.Col>{item.teacher}</Table.Col>
                                <Table.Col>{item.date}</Table.Col>
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
