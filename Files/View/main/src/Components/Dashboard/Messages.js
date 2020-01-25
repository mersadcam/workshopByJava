import React from 'react';
import messages from "./Messages.json";
import {Button, Card, Table} from "tabler-react";
import enrolledWorkshops from "./EnrolledWorkshops";

class Messages extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        const {messages} = this.props;

        return (
            <Card title="My Messages" className={'px-2'}>
                <Table responsive>
                    <Table.Header>
                        <Table.ColHeader>Sender</Table.ColHeader>
                        <Table.ColHeader>Preview</Table.ColHeader>
                        <Table.ColHeader>Date</Table.ColHeader>
                        <Table.ColHeader>{null}</Table.ColHeader>
                    </Table.Header>
                    <Table.Body>
                        {messages.map(item => (
                            <Table.Row>
                                <Table.Col>{item.sender}</Table.Col>
                                <Table.Col>{item.text}</Table.Col>
                                <Table.Col>{item.date}</Table.Col>
                                <Table.Col><Button outline size="sm" color="primary"> View </Button></Table.Col>
                            </Table.Row>
                        ))}

                    </Table.Body>
                </Table>
            </Card>
        );
    }
}

export default Messages;
