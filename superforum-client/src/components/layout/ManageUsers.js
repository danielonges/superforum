import React, { useContext, useState, useEffect } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import GitHubIcon from '@mui/icons-material/GitHub';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Sidebar from '../forum/Sidebar';
import Footer from '../forum/Footer';

import ForumContext from '../../context/forum/forumContext';
import UserContext from '../../context/user/userContext';
import { useLocation } from 'react-router';
import {
    FormControl,
    IconButton,
    InputAdornment,
    InputLabel,
    OutlinedInput,
    TextField
} from '@mui/material';
import Search from '@mui/icons-material/Search';
import UserCard from './UserCard';

const theme = createTheme();

const ManageUsers = () => {
    const forumContext = useContext(ForumContext);
    const userContext = useContext(UserContext);

    const [searchUsers, setSearchUsers] = useState('');

    const { user, users, getUsers } = userContext;

    useEffect(() => {
        getUsers(searchUsers);
        // eslint-disable-next-line
    }, [searchUsers]);

    const onChange = (e) => {
        setSearchUsers(e.target.value);
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <Typography variant='h4' gutterBottom>
                    Manage Users
                </Typography>
                <Divider sx={{ mb: 3 }} />
                <main>
                    <FormControl variant='outlined' sx={{ mb: 3 }}>
                        <InputLabel htmlFor='outlined-adornment-searchUsers'>
                            Search Username
                        </InputLabel>
                        <OutlinedInput
                            id='outlined-adornment-searchUsers'
                            name='searchUsers'
                            fullWidth
                            label='Search Username'
                            onChange={onChange}
                            endAdornment={
                                <InputAdornment position='end'>
                                    <IconButton edge='end'>
                                        <Search />
                                    </IconButton>
                                </InputAdornment>
                            }
                        />
                    </FormControl>
                    <Grid container spacing={4} columns={1}>
                        {users != null &&
                            users.map((user) => <UserCard user={user} />)}
                    </Grid>
                </main>
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};

export default ManageUsers;
