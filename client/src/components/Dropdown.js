import React from 'react';
import Select from 'react-select';
import styled from 'styled-components';

const categorylist = [
  { label: '아우터', value: '아우터' },
  { label: '상의', value: '상의' },
  { label: '하의', value: '하의' },
  { label: '원피스', value: '원피스' },
  { label: '모자', value: '모자' },
  { label: '신발', value: '신발' },
];
const Dropdown = () => {
  return (
    <SDropdown>
      <div className="container">
        <Select options={categorylist} placeholder="선택" />
      </div>
    </SDropdown>
  );
};

const SDropdown = styled.div`
  .container {
    background-color: #eee6ca;
  }
`;

export default Dropdown;
