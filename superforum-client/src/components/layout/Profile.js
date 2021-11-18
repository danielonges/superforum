import React, { useContext, useEffect, useState } from 'react';
import CssBaseline from '@mui/material/CssBaseline';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import Header from '../forum/Header';
import ForumThreadCard from '../forum/ForumThreadCard';
import Main from '../forum/Main';
import Sidebar from '../forum/Sidebar';
import Footer from '../forum/Footer';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';

import UserContext from '../../context/user/userContext';

import { withRouter } from 'react-router-dom';
import {
    Button,
    Card,
    CardContent,
    FormControl,
    IconButton,
    InputAdornment,
    InputLabel,
    OutlinedInput,
    TextField
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';

const theme = createTheme();

const Profile = (props) => {
    const userContext = useContext(UserContext);

    const { user, updateUser } = userContext;

    const [edit, setEdit] = useState(false);
    const [showPassword, setShowPassword] = useState(false);
    const [editUser, setEditUser] = useState({
        ...user
    });

    const onClickEdit = (e) => {
        e.preventDefault();
        setEdit(!edit);
    };

    const onSubmitSave = (e) => {
        e.preventDefault();
        setEdit(false);
        updateUser(editUser);
    };

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const onChange = (e) => {
        setEditUser({ ...user, [e.target.name]: e.target.value });
    };

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Container maxWidth='xl'>
                <Header />
                <Typography variant='h4' gutterBottom>
                    {user.username}'s Profile
                </Typography>

                <main>
                    <Divider sx={{ mb: 3 }} />
                    <Card
                        sx={{
                            display: 'flex',
                            alignItems: 'center',
                            padding: 3,
                            mb: 3
                        }}
                    >
                        <CardContent
                            sx={{ flex: 1 }}
                            component='form'
                            onSubmit={onSubmitSave}
                        >
                            <Typography
                                component='h2'
                                variant='h6'
                                sx={{ mb: 2 }}
                            >
                                {user.username}
                            </Typography>
                            <Grid container spacing={1} sx={{ mb: 3 }}>
                                {user.userType === 'ADMIN' && (
                                    <Grid item xs={12}>
                                        <Button
                                            variant='outlined'
                                            color='primary'
                                            sx={{ mb: 1 }}
                                        >
                                            Admin
                                        </Button>
                                    </Grid>
                                )}
                                <Grid item xs={12}>
                                    {edit ? (
                                        <TextField
                                            name='email'
                                            fullWidth
                                            id='email'
                                            label='Email'
                                            autoFocus
                                            defaultValue={user.email}
                                            type='email'
                                            sx={{ mb: 2 }}
                                            onChange={onChange}
                                        />
                                    ) : (
                                        <Typography variant='subtitle1'>
                                            Email: {user.email}
                                        </Typography>
                                    )}
                                </Grid>
                                <Grid item xs={12}>
                                    {edit ? (
                                        <TextField
                                            name='bio'
                                            fullWidth
                                            id='bio'
                                            label='Bio'
                                            multiline
                                            rows={4}
                                            autoFocus
                                            defaultValue={user.bio}
                                            sx={{ mb: 2 }}
                                            onChange={onChange}
                                        />
                                    ) : (
                                        <Typography variant='subtitle1'>
                                            Bio: {user.bio}
                                        </Typography>
                                    )}
                                </Grid>
                                {edit && (
                                    <Grid item xs={12}>
                                        <FormControl variant='outlined'>
                                            <InputLabel htmlFor='outlined-adornment-password'>
                                                Password
                                            </InputLabel>
                                            <OutlinedInput
                                                id='outlined-adornment-password'
                                                name='password'
                                                fullWidth
                                                label='Password'
                                                type={
                                                    showPassword
                                                        ? 'text'
                                                        : 'password'
                                                }
                                                autoFocus
                                                required
                                                defaultValue={user.password}
                                                onChange={onChange}
                                                endAdornment={
                                                    <InputAdornment position='end'>
                                                        <IconButton
                                                            aria-label='toggle password visibility'
                                                            onClick={
                                                                handleClickShowPassword
                                                            }
                                                            onMouseDown={
                                                                handleMouseDownPassword
                                                            }
                                                            edge='end'
                                                        >
                                                            {showPassword ? (
                                                                <VisibilityOff />
                                                            ) : (
                                                                <Visibility />
                                                            )}
                                                        </IconButton>
                                                    </InputAdornment>
                                                }
                                            />
                                        </FormControl>
                                    </Grid>
                                )}
                            </Grid>
                            {edit ? (
                                <Button variant='contained' type='submit'>
                                    Save Profile
                                </Button>
                            ) : (
                                <Button
                                    variant='contained'
                                    onClick={onClickEdit}
                                >
                                    Edit Profile
                                </Button>
                            )}
                        </CardContent>
                    </Card>
                </main>
            </Container>
            <Footer
                title='Footer'
                description='Something here to give the footer a purpose!'
            />
        </ThemeProvider>
    );
};
export default Profile;
