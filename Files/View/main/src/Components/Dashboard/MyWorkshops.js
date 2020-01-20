import React from 'react';
import {Button, Card, Icon, Table, Text, Dropdown, StampCard} from 'tabler-react';

class MyWorkshops extends React.Component {
    render() {
        return (
            <Card title="My Workshops">
                <Table
                    responsive
                    className="card-table table-vcenter"
                    headerItems={[
                        {content: "Workshop"},
                        {content: "Organizer"},
                        {content: "Start"},
                        {content: null},
                    ]}
                    bodyItems={[
                        {key: "1", item: [
                                {content: (<a href="#">Illustrator</a>)},
                                {content: (<a href="#" className="text-inherit">Mohammad Mahdian</a>)},
                                {content: "20 Jan 2020 / 15:00 PM"},
                                {content: (<Button outline size="sm" color="primary"> Manage </Button>),},
                            ],},
                        {key: "2", item: [
                                {content: (<a href="#">React JS</a>)},
                                {content: (<a href="#" className="text-inherit">Mersad Khalafi</a>)},
                                {content: "22 Jan 2020 / 17:00 PM"},
                                {content: (<Button outline size="sm" color="primary"> Manage </Button>),},
                            ],},
                        {key: "3", item: [
                                {content: (<a href="#">Java</a>)},
                                {content: (<a href="#" className="text-inherit">Ehsan Kaji</a>)},
                                {content: "24 Jan 2020 / 16:00 PM"},
                                {content: (<Button outline size="sm" color="primary"> Manage </Button>),},
                            ],},
                    ]}
                />
            </Card>
        );
    }
}

export default MyWorkshops;
