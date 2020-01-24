import React from 'react';
import {Button, Form} from "tabler-react";

class Search extends React.Component {
    render() {
        return (
                <Form.Group>
                    <Form.Input
                        icon="search"
                        placeholder="What do you want?"
                        position="append"
                    />
                </Form.Group>
        );
    }
}

export default Search;
